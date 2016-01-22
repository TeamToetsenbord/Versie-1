import psycopg2
import json
import os
import re
import sys
import smtplib
from email.mime.text import MIMEText

# This file if run by calling (on the command line of from another program) python, then the filename (email_generator.py), then the email address you want to send the email from, 
# then the password for the address you want to send the email from, then the email address you want to sent the email to, then the email type (authority, citygis, connections, controlroom), 
# and then, if you want to create a control room report, a unit_id, all separated by spaces
# The from email adress should be a gmail address
# Example: python email_generator.py me.example@gmail.com Pass123 you.whoisthis@hotmail.com authority

# installed: psycopg2, reportlab

# Creates the variables that will be assigned values based on the arguments used when running the script
email_address_from = ""
email_password = ""
email_address_to = ""
email_type = ""
unit_id = ""

# Assigns the values of the arguments used when running the script to the right variables
def main(argv):
	global email_address_from
	global email_password
	global email_address_to
	global email_type
	global unit_id

	if  len(sys.argv) == 6:
		email_address_from_arg, email_password_arg, email_address_to_arg, email_type_arg, unit_id_arg = argv
		unit_id = unit_id_arg
	elif len(sys.argv) == 5:
		email_address_from_arg, email_password_arg, email_address_to_arg, email_type_arg = argv
		
	email_address_from = email_address_from_arg
	email_password = email_password_arg
	email_address_to = email_address_to_arg
	email_type = email_type_arg

# This sets up a connection to the database
connection = psycopg2.connect(database="CityGis Data", user="postgres", password="toetsenbord", host="145.24.222.225", port="8000")
print "Opened database successfully"

# This creates a cursor with which to look through the database with
cur = connection.cursor()

# This function checks if the unit id given by the unit_id exists in the database
def check_unit_id():
	city_gis_data = []
	cur.execute("""SELECT unit_id FROM cars""")
	rows2 = cur.fetchall()
	for row in rows2:
		if unit_id == re.sub("[^0-9]", "", row[0]):
			return True
	return False

# These are the function used to retrieve data from the database and convert it to usable dictionaries
def get_authority_report_data():
	print "Getting authority report data..."
	
	most_visited_places = []
	most_stopped_places = []
	cur.execute("""SELECT to_json(mostvisited) as most_visited_places
		, to_json(moststopped) as most_stopped_places FROM 
		(SELECT DISTINCT latitude, longitude, COUNT(*) as amount_of_visits
		FROM car_position_data 
		WHERE event_date < (now() - interval '1 month')
		AND speed != '0'
		GROUP BY latitude, longitude 
		Order by amount_of_visits DESC
		LIMIT 5) as mostvisited,
		(SELECT DISTINCT latitude, longitude, COUNT(*) as amount_of_visits
		FROM car_position_data 
		WHERE event_date < (now() - interval '1 month')
		AND speed = '0'
		GROUP BY latitude, longitude 
		Order by amount_of_visits DESC
		LIMIT 5) as moststopped""")
	rows2 = cur.fetchall()
	for row in rows2:
		for counter, json_dict in enumerate(row, 1):
			if counter%2 == 0:
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"]) 
				data_row["amount of visits"] =  str(json_dict["amount_of_visits"])
				most_stopped_places.append(data_row)
			else: 
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"])
				data_row["amount of visits"] = str(json_dict["amount_of_visits"])
				most_visited_places.append(data_row)
	return (most_visited_places, most_stopped_places)

def get_CityGis_report_data():
	print "Getting CityGis report data..."

	city_gis_data = []
	cur.execute("""SELECT to_json(city_gis_data) AS city_gis_data_result
		FROM (
		SELECT gps_new_event_date as event_date, gps_data.unit_id, gps_speed, cs_speed, gps_course, cs_course, gps_latitude, gps_longitude, cs_latitude, cs_longitude
		FROM
		(	SELECT to_char(event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') as gps_new_event_date, 
			unit_id, 
			round(avg(speed)) as gps_speed, 
			round(avg(course)) as gps_course, 
			round(cast(avg(latitude) as decimal), 5) as gps_latitude, 
			round(cast(avg(longitude) as decimal), 5)as gps_longitude
			FROM car_position_data WHERE connection_type = 'gps' 
			GROUP BY unit_id, gps_new_event_date) gps_data
		INNER JOIN
		(	SELECT to_char(event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') as cs_new_event_date, 
			unit_id, 
			round(avg(speed)) as cs_speed, 
			round(avg(course)) as cs_course, 
			round(cast(avg(latitude) as decimal), 5) as cs_latitude, 
			round(cast(avg(longitude) as decimal), 5) as cs_longitude
			FROM car_position_data WHERE connection_type = 'car system' 
			GROUP BY unit_id, cs_new_event_date) cs_data
		ON gps_data.gps_new_event_date = cs_data.cs_new_event_date
		AND
		cs_data.unit_id = gps_data.unit_id
		WHERE  cs_speed != gps_speed OR cs_course != gps_course OR cs_latitude != gps_latitude OR cs_longitude != cs_longitude
		ORDER BY (cs_speed != gps_speed AND cs_course != gps_course AND cs_latitude != gps_latitude AND cs_longitude != cs_longitude) DESC LIMIT 10) AS city_gis_data""")
	rows2 = cur.fetchall()
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["gps latitude"] = str(json_dict["gps_latitude"])
			data_row["gps longitude"] = str(json_dict["gps_longitude"])
			data_row["gps course"] = str(json_dict["gps_course"])
			data_row["gps speed"] = str(json_dict["gps_speed"])
			data_row["cs latitude"] = str(json_dict["cs_latitude"])
			data_row["cs longitude"] = str(json_dict["cs_longitude"])
			data_row["cs course"] = str(json_dict["cs_course"])
			data_row["cs speed"] = str(json_dict["cs_speed"])
			city_gis_data.append(data_row)
	return city_gis_data

# Connections report queries
def get_overall_connections_report_data():
	print "Getting connections report data..."
	
	best_overall_connection_locations = []
	worst_overall_connection_locations = []
	cur.execute("""SELECT to_json(best_overall_connection_locations) as best_overall_connection_locations, 
		to_json(worst_overall_connection_locations)as worst_overall_connection_locations
		FROM 
			(SELECT latitude, longitude 
			FROM (
			SELECT  DISTINCT latitude, longitude, count(*)
			FROM car_position_data cpd INNER JOIN 
			(SELECT DISTINCT event_date, unit_id FROM overall_connections WHERE connected != false) oc
			ON cpd.event_date = oc.event_date AND cpd.unit_id = oc.unit_id
			GROUP BY latitude, longitude
			ORDER BY count(*) desc
			LIMIT 10) best_overall_connection_locations_table) as best_overall_connection_locations,
			(SELECT latitude, longitude 
			FROM (
			SELECT  DISTINCT latitude, longitude, count(*)
			FROM car_position_data cpd INNER JOIN 
			(SELECT DISTINCT event_date, unit_id FROM overall_connections WHERE connected != false) oc
			ON cpd.event_date = oc.event_date AND cpd.unit_id = oc.unit_id
			GROUP BY latitude, longitude
			ORDER BY count(*) asc
			LIMIT 10 ) worst_overall_connection_locations_table ) as worst_overall_connection_locations""")
	rows2 = cur.fetchall()
	for row in rows2:
		for counter, json_dict in enumerate(row, 1):
			if counter%2 == 0:
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"]) 
				worst_overall_connection_locations.append(data_row)
			else: 
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"])
				best_overall_connection_locations.append(data_row)
	return (best_overall_connection_locations, worst_overall_connection_locations)

def get_hsdpa_connections_report_data():
	print "Getting hsdpa connection data..."
	
	best_hsdpa_connection_locations = []
	worst_hsdpa_connection_locations = []
	cur.execute("""SELECT 	to_json(best_hsdpa_connection_locations)as best_hsdpa_connection_locations,
	to_json(worst_hsdpa_connection_locations)as worst_hsdpa_connection_locations
	FROM
	(SELECT latitude, longitude FROM  (
	SELECT DISTINCT latitude, longitude, COUNT(*)
	FROM car_position_data cpd INNER JOIN 
		(SELECT date_time_start_minute, unit_id
		FROM hsdpa_connections 
		WHERE squal IS NOT null 
		AND rssi IS NOT null AND rscp IS NOT null 
		AND srxlev IS NOT null 
		AND number_of_connections IS NOT null
		ORDER BY number_of_connections desc LIMIT 10) hc
		ON to_char(cpd.event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') = 
		to_char(hc.date_time_start_minute ::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00')
		AND cpd.unit_id = hc.unit_id
		GROUP BY latitude, longitude
		ORDER BY COUNT(*) DESC
		LIMIT 10) best_hsdpa_connection_locations_table) as best_hsdpa_connection_locations,
	(SELECT latitude, longitude FROM  (
	SELECT DISTINCT latitude, longitude, COUNT(*)
	FROM car_position_data cpd INNER JOIN 
		(SELECT date_time_start_minute, unit_id
		FROM hsdpa_connections 
		WHERE squal IS NOT null 
		AND rssi IS NOT null AND rscp IS NOT null 
		AND srxlev IS NOT null 
		AND number_of_connections IS NOT null
		ORDER BY number_of_connections asc LIMIT 10) hc
		ON to_char(cpd.event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') = 
		to_char(hc.date_time_start_minute ::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00')
		AND cpd.unit_id = hc.unit_id
		GROUP BY latitude, longitude
		ORDER BY COUNT(*) DESC
		LIMIT 10) worst_hsdpa_connection_locations_table) as worst_hsdpa_connection_locations""")
	rows2 = cur.fetchall()
	for row in rows2:
		for counter, json_dict in enumerate(row, 1):
			if counter%2 == 0:
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"]) 
				worst_hsdpa_connection_locations.append(data_row)
			else: 
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"])
				best_hsdpa_connection_locations.append(data_row)
	return (best_hsdpa_connection_locations, worst_hsdpa_connection_locations)
	
def get_tcp_connections_report_data():
	print "Getting tcp connection data..."
	
	best_tcp_connections = []
	worst_tcp_connections = []
	cur.execute("""SELECT	to_json(best_tcp_connections)as best_tcp_connections,
	to_json(worst_tcp_connections)as worst_tcp_connections FROM
	(SELECT latitude, longitude FROM  (
	SELECT DISTINCT latitude, longitude, COUNT(*)
	FROM car_position_data cpd INNER JOIN 
		(SELECT date_time_start_minute, unit_id, round_trip_latency
		FROM tcp_client_connections
		WHERE outstanding_sends = 0 
		AND round_trip_latency IS NOT null
		ORDER BY round_trip_latency asc
		LIMIT 10) tcc	ON to_char(cpd.event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') = 
		to_char(tcc.date_time_start_minute ::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00')
		AND cpd.unit_id = tcc.unit_id
		GROUP BY latitude, longitude
		ORDER BY COUNT(*) DESC
		LIMIT 10) best_tcp_connections_table ) as best_tcp_connections,
	(SELECT latitude, longitude FROM  (
	SELECT DISTINCT latitude, longitude, COUNT(*)
	FROM car_position_data cpd INNER JOIN 
		(SELECT date_time_start_minute, unit_id, round_trip_latency
		FROM tcp_client_connections
		WHERE outstanding_sends >= 0 
		AND round_trip_latency IS NOT null
		ORDER BY round_trip_latency desc
		LIMIT 10) tcc	ON to_char(cpd.event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') = 
		to_char(tcc.date_time_start_minute ::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00')
		AND cpd.unit_id = tcc.unit_id
		GROUP BY latitude, longitude
		ORDER BY COUNT(*) DESC
		LIMIT 10) worst_tcp_connections_table) as worst_tcp_connections""")
	rows2 = cur.fetchall()
	for row in rows2:
		for counter, json_dict in enumerate(row, 1):
			if counter%2 == 0:
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"]) 
				worst_tcp_connections.append(data_row)
			else: 
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"])
				best_tcp_connections.append(data_row)
	return (best_tcp_connections, worst_tcp_connections)

# Control room queries
def get_highest_speed_control_room_data():
	print "Getting highest speed locations..."
	
	highest_speed_locations = []
	cur.execute("""SELECT row_to_json(t) as Highest_speed_locations
	FROM (
		SELECT unit_id, MAX(speed), longitude, latitude
		FROM car_position_data
		GROUP BY unit_id, longitude, latitude, speed
		ORDER BY speed DESC
	) t LIMIT 10;""")
	rows2 = cur.fetchall()
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["unit id"] = str(json_dict["unit_id"])
			data_row["longitude"] = str(json_dict["longitude"])
			data_row["latitude"] = str(json_dict["latitude"])
			data_row["max"] = str(json_dict["max"])
			highest_speed_locations.append(data_row)
	return highest_speed_locations

def get_least_visited_control_room_data():
	print "Getting least visited locations..."
	
	least_visited_locations = []
	cur.execute("""SELECT to_json(leastvisited) as most_Visisted_places
	FROM (SELECT DISTINCT latitude, longitude, COUNT(*) as amount_of_visits
	FROM car_position_data 
	WHERE event_date < (now() - interval '1 month')
	AND speed != '0'
	GROUP BY latitude, longitude 
	Order by amount_of_visits ASC
	LIMIT 10) as leastvisited;""")
	rows2 = cur.fetchall()
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["longitude"] = str(json_dict["longitude"])
			data_row["latitude"] = str(json_dict["latitude"])
			data_row["amount of visits"] = str(json_dict["amount_of_visits"])
			least_visited_locations.append(data_row)
	return least_visited_locations

def get_most_common_driving_times_control_room_data():
	print "Getting most common driving times..."
	
	common_driving_times = []
	cur.execute("""SELECT row_to_json(t) AS common_drive_times
	FROM (
		SELECT EXTRACT(HOUR FROM event_date) AS hour_, count(*) AS Times_appeared
		FROM car_status_events 
		WHERE powerstatus = TRUE 
		 AND unit_id = '""" + unit_id + """'
		GROUP BY hour_
		ORDER BY Times_appeared DESC)t ;""")
	rows2 = cur.fetchall()
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["hour"] = str(json_dict["hour_"])
			data_row["times"] = str(json_dict["times_appeared"])
			common_driving_times.append(data_row)
	return common_driving_times
	
def get_least_common_driving_times_control_room_data():
	print "Getting least common driving times..."
	
	uncommon_driving_times = []
	cur.execute("""	SELECT row_to_json(t) AS common_drive_times
	FROM (
		SELECT EXTRACT(HOUR FROM event_date) AS hour_, count(*) AS Times_appeared
		FROM car_status_events 
		WHERE powerstatus = TRUE 
		 AND unit_id = '""" + unit_id + """'
		GROUP BY hour_
		ORDER BY Times_appeared ASC)t ;""")
	rows2 = cur.fetchall()	
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["hour"] = str(json_dict["hour_"])
			data_row["times"] = str(json_dict["times_appeared"])
			uncommon_driving_times.append(data_row)
	return uncommon_driving_times
	
def get_locations_longest_stays_control_room_data():
	print "Getting locations where the user has stayed the longest..."

	places_longest_stays = []
	cur.execute("""SELECT row_to_json(results)
	FROM (
		SELECT latitude, longitude
		FROM car_position_data	cpd
		INNER JOIN
		(SELECT event_date, unit_id 
		 FROM car_status_events 
		 WHERE powerstatus = false
		 AND unit_id = '""" + unit_id + """' 
		) cse
		 ON cse.event_date = cpd.event_date AND cse.unit_id = cpd.unit_id
		 GROUP BY latitude, longitude 
		 ORDER BY count(*) DESC
		 LIMIT 10) AS results""")
	rows2 = cur.fetchall()
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["longitude"] = str(json_dict["longitude"])
			data_row["latitude"] = str(json_dict["latitude"])
			places_longest_stays.append(data_row)
	return places_longest_stays

# These are the methods used to create the contents of the e-mails
def create_authority_report_email():
	message_list = []
	most_visited_places, most_stopped_places = get_authority_report_data()
	message_list.append("Most visited places: ")
	for dict in most_visited_places:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n" +
		"amount of visits: " + dict["amount of visits"] + "\n")
	message_list.append("Most stopped places: ")
	for dict in most_stopped_places:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n" +
		"amount of visits: " + dict["amount of visits"] + "\n")
	
	send_email("\n".join(message_list), "Authority Report email")
	
def create_CityGis_report_email():
	message_list = []
	city_gis_data = get_CityGis_report_data()
	for dict in city_gis_data:
		message_list.append("Car system: " + "\n" +
		"cs latitude: " + dict["cs latitude"] + "\n" +
		"cs longitude: " + dict["cs longitude"] + "\n" +
		"cs speed: " + dict["cs speed"] + "\n" +
		"cs course: " + dict["cs course"] + "\n" +
		"GPS: " + "\n" +
		"gps latitude: " + dict["gps latitude"] + "\n" +
		"gps longitude: " + dict["gps longitude"] + "\n" +
		"gps speed: " + dict["gps speed"] + "\n" +
		"gps course: " + dict["gps course"] + "\n")

	send_email("\n".join(message_list), "CityGis Report email")
	
def create_connections_report_email():
	message_list = []
	best_overall_connection_locations, worst_overall_connection_locations = get_overall_connections_report_data()
	message_list.append("Overall connections")
	message_list.append("Best: ")
	for dict in best_overall_connection_locations:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")
	message_list.append("Worst: ")
	for dict in worst_overall_connection_locations:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")

	best_hsdpa_connection_locations, worst_hsdpa_connection_locations = get_hsdpa_connections_report_data()
	message_list.append("hspda connections")
	message_list.append("Best: ")
	for dict in best_hsdpa_connection_locations:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")
	message_list.append("Worst: ")
	for dict in worst_hsdpa_connection_locations:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")
		
	best_tcp_connections, worst_tcp_connections = get_tcp_connections_report_data()
	message_list.append("tcp connections")
	message_list.append("Best: ")
	for dict in best_tcp_connections:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")
	message_list.append("Worst: ")
	for dict in worst_tcp_connections:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		"longitude: " + dict["longitude"] + "\n")	
	
	send_email("\n".join(message_list), "Connections Report email")

def create_control_room_report_email():
	message_list = []
	highest_speed_locations = get_highest_speed_control_room_data()
	message_list.append("Unit_id used:" + unit_id)
	message_list.append("Highest speed: ")
	for dict in highest_speed_locations:
		message_list.append( "unit id: " + dict["unit id"] + "\n" +
		 "latitude: " + dict["latitude"] + "\n" +
		 "longitude: " + dict["longitude"] + "\n" +
		 "max speed: " + dict["max"] + "\n")

	least_visited_locations = get_least_visited_control_room_data()
	message_list.append("Least visited locations: ")
	for dict in least_visited_locations:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		 "longitude: " + dict["longitude"] + "\n" +
		 "amount of visits: " + dict["amount of visits"] + "\n")
		
	common_driving_times = get_most_common_driving_times_control_room_data()
	message_list.append("Most common driving times: ")
	for dict in common_driving_times:
		message_list.append("hour: " + dict["hour"] + "\n" +
		 "times: " + dict["times"] + "\n")
		
	uncommon_driving_times = get_least_common_driving_times_control_room_data()
	message_list.append("Least common driving times: ")
	for dict in uncommon_driving_times:
		message_list.append("hour: " + dict["hour"] + "\n" +
		 "times: " + dict["times"] + "\n")

	places_longest_stays = get_locations_longest_stays_control_room_data()
	message_list.append("Longest stays: ")
	for dict in places_longest_stays:
		message_list.append("latitude: " + dict["latitude"] + "\n" +
		 "longitude: " + dict["longitude"] + "\n")
	
	send_email("\n".join(message_list), "Control Room Report email")
	
# Sends an email with the previously specified contents from and to the email addresses specified by the user
def send_email(content, subject):
	msg = MIMEText(content)
	msg['Subject'] = subject
	msg['From'] = email_address_from
	msg['To'] = email_address_to
	try:
		s = smtplib.SMTP('smtp.gmail.com', 587)
		s.starttls()
		s.ehlo()
		s.login(email_address_from, email_password)
		s.sendmail(email_address_from, email_address_to, msg.as_string())
		s.quit()
	except smtplib.SMTPException:
	   print "Unable to send email. Something may be wrong with the email addresses or password you put in."

# Decides which report to make according to the user input
def pick_report():
	print "Picking report type for email... " + email_type
	
	if email_type == "authority":
		create_authority_report_email()
	elif email_type == "citygis":
		create_CityGis_report_email()
	elif email_type == "connections":
		create_connections_report_email()
	elif email_type == "controlroom":	
		if unit_id != "" and check_unit_id() == True:
			create_control_room_report_email()
		else:
			sys.exit("You didn't provide a (correct) unit_id")
	else:
		sys.exit("You didn't provide any arguments. You should provide a filename, directory, email_type and optionally a unit_id")
	
# Call the starting functions
if __name__ == "__main__":
	main(sys.argv[1:])
	pick_report()

# This closes the connection to the database
connection.commit()
connection.close()
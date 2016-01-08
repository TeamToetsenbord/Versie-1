import psycopg2
import json
import reportlab

# installed: psycopg2
# reposrtlab with easy_install reportlab, then downloaded c++ thingy from https://www.microsoft.com/en-us/download/details.aspx?id=44266

connection = psycopg2.connect(database="CityGis Data", user="postgres", password="toetsenbord", host="145.24.222.225", port="8000")
print "Opened database successfully"

cur = connection.cursor()
def fetch_car_data():
	cur.execute("SELECT unit_id from cars")
	rows = cur.fetchall()
	for row in rows:
		print "unit_id = ", row[0]#, "\n"

def get_authority_report_data():
	counter = 1
	most_visited_places = []
	amount_of_visits = []
	cur.execute("""SELECT to_json(mostvisited) as most_Visisted_places
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
	rows2=cur.fetchall()
	for row in rows2:
		for json_dict in row:
			if counter%2 == 0:
			#print json_dict
			#json_thingy = '{"latitude":5.62035390955791,"longitude":51.4807393702185,"amount_of_visits":17}'
			# data = json.load(json_dict) <- werkt niet, waarom? Is het uberhaupt nodig? json.dumps kan wel, maar maakt er een string van
				amount_of_visits.append("Amount of visits: \n" +
				"Latitude: " + str(json_dict["latitude"]) + "\n" +
				"Longitude: " + str(json_dict["longitude"]) + "\n" +
				"Amount of visits: " + str(json_dict["amount_of_visits"]) + "\n" + "-=-")
			else: 
				most_visited_places.append("Most visited places: \n" +
				"Latitude: " + str(json_dict["latitude"]) + "\n" +
				"Longitude: " + str(json_dict["longitude"]) + "\n" +
				"Amount of visits: " + str(json_dict["amount_of_visits"]) + "\n" + "-=-")
			counter += 1
			#TODO: Zorgen dat hij 2 verschillende lijsten/weergaves heeft voor de verschillende rijden
	for item in most_visited_places:
		print item
	
	for item in amount_of_visits:
		print item
		#print row[0]
		#				 json.JSONDecoder([encoding[, object_hook[, parse_float[, parse_int[, parse_constant[, strict[, object_pairs_hook]]]]]]])
		#data_test = json.JSONDecoder([unicode[ [ [ [ [ [ ]]]]]]])
		#data = json.loads(row[])
		#print(data)
	
# json_thingy = '{"latitude":5.62035390955791,"longitude":51.4807393702185,"amount_of_visits":17}'
# data = json.load(json_thingy)
# print data


def get_CityGis_report_data():
	counter = 1
	most_visited_places = []
	amount_of_visits = []
	cur.execute("""SELECT to_json(city_gis_data) AS city_gis_data_result
		FROM (
		SELECT gps_new_event_date as event_date, gps_data.unit_id, gps_speed, cs_speed, gps_course, cs_course, gps_location, cs_location
		FROM
		(	SELECT to_char(event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') as gps_new_event_date, 
			unit_id, 
			round(avg(speed)) as gps_speed, 
			round(avg(course)) as gps_course, 
			(round(cast(avg(latitude) as decimal), 5), round(cast(avg(longitude) as decimal), 5)) as gps_location
			FROM car_position_data WHERE connection_type = 'gps' 
			GROUP BY unit_id, gps_new_event_date) gps_data
		INNER JOIN
		(	SELECT to_char(event_date::timestamp without time zone, 'YYYY-MM-DD HH24:MI:00') as cs_new_event_date, 
			unit_id, 
			round(avg(speed)) as cs_speed, 
			round(avg(course)) as cs_course, 
			(round(cast(avg(latitude) as decimal),5), round(cast(avg(longitude) as decimal),5) ) as cs_location
			FROM car_position_data WHERE connection_type = 'car system' 
			GROUP BY unit_id, cs_new_event_date) cs_data
		ON gps_data.gps_new_event_date = cs_data.cs_new_event_date
		AND
		cs_data.unit_id = gps_data.unit_id
		WHERE  cs_speed != gps_speed OR cs_course != gps_course OR cs_location != gps_location
		ORDER BY (cs_speed != gps_speed AND cs_course != gps_course AND cs_location != gps_location) DESC ) AS city_gis_data""")
	rows2=cur.fetchall()
	for row in rows2:
		for json_dict in row:
			print json_dict


#from reportlab.pdfgen import canvas  
#from reportlab.lib.units import cm  
from reportlab.pdfgen import canvas  
from reportlab.lib.units import cm  
c = canvas.Canvas("hello.pdf")  
c.drawString(9*cm, 22*cm, "Hello World!")  
c.showPage()  
c.save()
			
get_authority_report_data()	
#get_CityGis_report_data()


print "Operation done successfully";

connection.commit()
connection.close()
import psycopg2
import json
import reportlab
import time

# installed: psycopg2
# reposrtlab with easy_install reportlab, then downloaded c++ thingy from https://www.microsoft.com/en-us/download/details.aspx?id=44266

# This sets up a connection to the database
connection = psycopg2.connect(database="CityGis Data", user="postgres", password="toetsenbord", host="145.24.222.225", port="8000")
print "Opened database successfully"

# This creates a cursor with which to look through the database with
cur = connection.cursor()
def fetch_car_data():
	cur.execute("SELECT unit_id from cars")
	rows = cur.fetchall()
	for row in rows:
		print "unit_id = ", row[0]#, "\n"

# These are the function used to
def get_authority_report_data():
	print "Getting authority report data..."
	
	counter = 1
	most_visited_places = []
	amount_of_visits = []
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
	rows2=cur.fetchall()
	for row in rows2:
		for json_dict in row:
			if counter%2 == 0:
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"]) 
				data_row["amount of visits"] =  str(json_dict["amount_of_visits"])
				
				amount_of_visits.append(data_row)
			else: 
				data_row = {}
				data_row["latitude"] = str(json_dict["latitude"])
				data_row["longitude"] = str(json_dict["longitude"])
				data_row["amount of visits"] = str(json_dict["amount_of_visits"])
				
				most_visited_places.append(data_row)
			counter += 1	
	return (most_visited_places, amount_of_visits)

def get_CityGis_report_data():
	print "Getting CityGis report data..."

	city_gis_data = []
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
	rows2=cur.fetchmany(5)	#TODO fetchall
	for row in rows2:
		for json_dict in row:
			data_row = {}
			data_row["gps location f1"] = str(json_dict["gps_location"]["f1"])
			data_row["gps location f2"] = str(json_dict["gps_location"]["f2"])
			data_row["gps course"] = str(json_dict["gps_course"])
			data_row["gps speed"] = str(json_dict["gps_speed"])
			data_row["cs location f1"] = str(json_dict["cs_location"]["f1"])
			data_row["cs location f2"] = str(json_dict["cs_location"]["f2"])
			data_row["cs course"] = str(json_dict["cs_course"])
			data_row["cs speed"] = str(json_dict["cs_speed"])
			
			city_gis_data.append(data_row)
	return city_gis_data
	#TODO: Finish this

#TODO move these import thingies
#import reportlab attributes, need to be imported separately
from reportlab.pdfgen import canvas  
from reportlab.lib.units import cm  
from reportlab.platypus import Table, TableStyle
from reportlab.lib import colors

def create_pdf_report():
	c = canvas.Canvas("hello2.pdf") 
	draw_front_page(c, "Hello")
	c.setFont("Helvetica", 12)
	c.drawString(9*cm, 22*cm, "Hello World!")  								# Fonts: can only use the standard 14 fonts that come with acrobat reader
	c.save()

def create_CityGis_report():
	c = canvas.Canvas("CityGis Report5.pdf") 					# TODO: put date in title?
	draw_front_page(c, "CityGis Report")
	c.setFont("Helvetica", 12)	
	c.drawString(9*cm, 22*cm, "Hello World!")
	#Table(get_CityGis_report_data(), colWidths=2*cm, rowHeights=2*cm, style=None, splitByRow=1)
	#test_table(c)
	print "Created CityGis report"
	c.save()
	
def draw_front_page(c, title):
	c.setFont("Helvetica", 18)
	c.drawString(8*cm, 22*cm, title)
	c.setFont("Helvetica", 12)
	c.drawString(8*cm, 21*cm, "test")
	c.drawString(8*cm, 20*cm, "Created on " + (time.strftime("%d/%m/%Y")) + " at " + (time.strftime("%H:%M")))
	c.showPage()

# def test_table(c):
	# # container for the 'Flowable' objects
	# elements = []
	 
	# data= [['00', '01', '02', '03', '04'],
		   # ['10', '11', '12', '13', '14'],
		   # ['20', '21', '22', '23', '24'],
		   # ['30', '31', '32', '33', '34']]
	# t=Table(data,5*[2*cm], 4*[2*cm])
	# elements.append(t)
	# # write the document to disk
	# t.drawOn(c, 1*cm, 9*cm)
	
#testing get_authority_report_data function
most_visited_places, amount_of_visits = get_authority_report_data()
for dict in most_visited_places:
	print "latitude: " + dict["latitude"]
	print "longitude: " + dict["longitude"]
	print "amount of visits: " + dict["amount of visits"]
	print "-=-"
for dict in amount_of_visits:
	print "latitude: " + dict["latitude"]
	print "longitude: " + dict["longitude"]
	print "amount of visits: " + dict["amount of visits"]
	print "-=-"

print "Operation done successfully";

#testing get_CityGis_report_data function
city_gis_data = get_CityGis_report_data()
for dict in city_gis_data:
	print "cs location f1: " + dict["cs location f1"]
	print "cs location f2: " + dict["cs location f2"]
	print "cs speed: " + dict["cs speed"]
	print "cs course: " + dict["cs course"]
	print "gps location f1: " + dict["gps location f1"]
	print "gps location f2: " + dict["gps location f2"]
	print "gps speed: " + dict["gps speed"]
	print "gps course: " + dict["gps course"]
	print "-=-"

print "Operation done successfully";

create_CityGis_report()
#create_pdf_report()

# TODO: split into multiple classes?
# TODO: move this?

# This closes the connection to the database
connection.commit()
connection.close()
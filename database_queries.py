import psycopg2
import json

connection = psycopg2.connect(database="CityGis Data", user="postgres", password="toetsenbord", host="145.24.222.225", port="8000")
print "Opened database successfully"

cur = connection.cursor()
def fetch_car_data():
	cur.execute("SELECT unit_id from cars")
	rows = cur.fetchall()
	for row in rows:
		print "unit_id = ", row[0]#, "\n"

def get_data():
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
			#print json_dict
			#json_thingy = '{"latitude":5.62035390955791,"longitude":51.4807393702185,"amount_of_visits":17}'
			# data = json.load(json_dict) <- werkt niet, waarom? Is het uberhaupt nodig? json.dups kan wel, maar maakt er een string van
			print "Latitude: " + str(json_dict["latitude"])
			print "Longitude:" + str(json_dict["longitude"])
			print "Amount of visits: " + str(json_dict["amount_of_visits"])
			#TODO: Zorgen dat hij 2 verschillende lijsten/weergaves heeft voor de verschillende rijden
		print "-=-"
		#print row[0]
		#				 json.JSONDecoder([encoding[, object_hook[, parse_float[, parse_int[, parse_constant[, strict[, object_pairs_hook]]]]]]])
		#data_test = json.JSONDecoder([unicode[ [ [ [ [ [ ]]]]]]])
		#data = json.loads(row[])
		#print(data)
	
# json_thingy = '{"latitude":5.62035390955791,"longitude":51.4807393702185,"amount_of_visits":17}'
# data = json.load(json_thingy)
# print data

get_data()	
	
print "Operation done successfully";

connection.commit()
connection.close()
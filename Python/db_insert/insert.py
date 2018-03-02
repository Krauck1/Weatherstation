import mysql.connector
from datetime import date, datetime, timedelta
import time
import random

mysql_host = "db"
mysql_user = "root"
mysql_password = "passme"
mysql_db = "weather"
	
def main():
	min = 1
	startTime = datetime.now();
	while True:
		try:
			conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
		except mysql.connector.Error as err:
			if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
				print("Something is wrong with your user name or password")
			elif err.errno == errorcode.ER_BAD_DB_ERROR:
				print("Database does not exist")
			else:
				print(err)
		
		statement = ("insert into WEATHER_MEASUREMENT(ambient_temperature, ground_temperature, air_quality, air_pressure, humidity, wind_direction, wind_speed, wind_gust_speed, rainfall, created)"
					"values (%(ambient_temperature)s, %(ground_temperature)s, 1, 10, %(humidity)s, 20, %(wind_speed)s, %(wind_gust_speed)s, %(rainfall)s, %(created)s)")
		newDate = startTime + timedelta(minutes=min)
		min += 5
		data = {
			'ambient_temperature' : random.randint(-10, 30),
			'ground_temperature' : random.randint(-10, 30),
			'humidity' : random.randint(0, 100),
			'wind_speed' : random.randint(0, 40),
			'wind_gust_speed' : random.randint(0, 40),
			'rainfall' : random.randint(0, 100),
			'created' : newDate 
		}
		#datetime.datetime.now()
		cursor = conn.cursor()
		cursor.execute(statement, data)
		conn.commit()
		
		cursor.close()
		conn.close()
		time.sleep(5)
		
if __name__ == "__main__":
    main()

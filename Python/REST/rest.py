from flask import Flask, url_for
from flask import Response
import flask
import web
import json
#import responses
import requests
import mysql.connector
import logging

app = Flask(__name__)

log = logging.getLogger('werkzeug') #flask logger
log.setLevel(logging.CRITICAL)

mysql_host = "localhost"
mysql_user = "root"
mysql_password = "passme"
mysql_db = "weather"


class weather_measurement(object):
    ambient_temperature = float(0)
    ground_temperature = float(0)
    air_quality = float(0)
    humidity = float(0)
    wind_speed = float(0)
    wind_gust_speed = float(0)
    rainfall = float(0)
    created = ""
    air_pressure = float(0)

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, 
            sort_keys=True, indent=4)

class MyEncoder(json.JSONEncoder):
    def default(self, obj):
        if not isinstance(obj, weather_measurement):
            return super(MyEncoder, self).default(obj)

        return obj.__dict__

#get all measurements
@app.route('/all', methods = ['GET'])
def find_all():
    try:
        conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    cursor = conn.cursor() #create cursor for select
    res = ""
    query = ("SELECT ambient_temperature, ground_temperature, air_quality, humidity, wind_speed, wind_gust_speed,rainfall, created, air_pressure FROM WEATHER_MEASUREMENT")
    cursor.execute(query)
    data = cursor.fetchall()
        
    list = []
    for row in data:
        mes = weather_measurement()
        mes.ambient_temperature = float(row[0])
        mes.ground_temperature = float(row[1])
        mes.air_quality = float(row[2])
        mes.humidity = float(row[3])
        mes.wind_speed = float(row[4])
        mes.wind_gust_speed = float(row[5])
        mes.rainfall = float(row[6])
        mes.created = str(row[7])
        mes.air_pressure = float(row[8])
        list.append(mes)
    conn.close
    cursor.close()
    resp = Response(json.dumps(list, cls=MyEncoder), status=200, mimetype='application/json')
    resp.headers["Access-Control-Allow-Origin"] = '*'
    return resp

#get last measurement
@app.route('/last', methods = ['GET'])
def get_last():
    try:
        conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    cursor = conn.cursor() #create cursor for select
    res = ""
    query = ("SELECT ambient_temperature, ground_temperature, air_quality, humidity, wind_speed, wind_gust_speed,rainfall, created, air_pressure FROM WEATHER_MEASUREMENT WHERE created = (SELECT MAX(created) FROM WEATHER_MEASUREMENT);")
    cursor.execute(query)
    data = cursor.fetchall()    
    list = []
    if len(data) == 1:
        row = data[0]
        mes = weather_measurement()
        mes.ambient_temperature = float(row[0])
        mes.ground_temperature = float(row[1])
        mes.air_quality = float(row[2])
        mes.humidity = float(row[3])
        mes.wind_speed = float(row[4])
        mes.wind_gust_speed = float(row[5])
        mes.rainfall = float(row[6])
        mes.created = str(row[7])
        mes.air_pressure = float(row[8])
        conn.close()
        cursor.close()
        resp = Response(json.dumps(mes, cls=MyEncoder), status=200, mimetype='application/json')
        resp.headers["Access-Control-Allow-Origin"] = '*'
        return resp
    else:
        conn.close()
        cursor.close()
        resp = Response(json.dumps(list, cls=MyEncoder), status=500, mimetype='text/html')
        resp.headers["Access-Control-Allow-Origin"] = '*'
        return resp

#get all measurements between two dates
@app.route('/between/<fromDate>/<toDate>', methods = ['GET'])
def between_two_dates(fromDate, toDate):
    try:
        conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    cursor = conn.cursor() #create cursor for select
    res = ""
    query = ("SELECT ambient_temperature, ground_temperature, air_quality, humidity, wind_speed, wind_gust_speed,rainfall, created," +
            "air_pressure FROM WEATHER_MEASUREMENT WHERE created >= %s AND created <= %s")
    cursor.execute(query, (fromDate, toDate))
    data = cursor.fetchall()
    list = []
    for row in data:
        mes = weather_measurement()
        mes.ambient_temperature = float(row[0])
        mes.ground_temperature = float(row[1])
        mes.air_quality = float(row[2])
        mes.humidity = float(row[3])
        mes.wind_speed = float(row[4])
        mes.wind_gust_speed = float(row[5])
        mes.rainfall = float(row[6])
        mes.created = str(row[7])
        mes.air_pressure = float(row[8])
        list.append(mes)
    conn.close();
    cursor.close()
    resp = Response(json.dumps(list, cls=MyEncoder), status=200, mimetype='application/json')
    resp.headers["Access-Control-Allow-Origin"] = '*'
    return resp

#get all measurements n seconds ago
@app.route('/from_now/<int:seconds>', methods = ['GET'])
def seconds_from_now(seconds):
    try:
        conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    cursor = conn.cursor() #create cursor for select
    res = ""
    query = ("SELECT ambient_temperature, ground_temperature, air_quality, humidity, wind_speed, wind_gust_speed,rainfall, created, air_pressure FROM WEATHER_MEASUREMENT " + 
            "WHERE created >= from_unixtime(unix_timestamp(current_timestamp) - %s) AND created <= current_timestamp()")
    cursor.execute(query, (seconds,))
    data = cursor.fetchall()    
    list = []
    for row in data:
        mes = weather_measurement()
        mes.ambient_temperature = float(row[0])
        mes.ground_temperature = float(row[1])
        mes.air_quality = float(row[2])
        mes.humidity = float(row[3])
        mes.wind_speed = float(row[4])
        mes.wind_gust_speed = float(row[5])
        mes.rainfall = float(row[6])
        mes.created = str(row[7])
        mes.air_pressure = float(row[8])
        list.append(mes)
    resp = Response(json.dumps(list, cls=MyEncoder), status=200, mimetype='application/json')
    resp.headers["Access-Control-Allow-Origin"] = '*'
    conn.close()
    cursor.close()
    return resp

#get the measurement closest to a specific date    
@app.route('/at_date/<date>', methods = ['GET'])
def at_date(date):
    try:
        conn =  mysql.connector.connect(host=mysql_host, user=mysql_user, passwd=mysql_password, db=mysql_db)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
    cursor = conn.cursor() #create cursor for select
    res = ""
    query = ("SELECT ambient_temperature, ground_temperature, air_quality, humidity, wind_speed, wind_gust_speed,rainfall, created, air_pressure FROM WEATHER_MEASUREMENT " + 
            "ORDER BY ABS(unix_timestamp(%s) - unix_timestamp(CREATED)) LIMIT 1")
    cursor.execute(query, (date,))
    data = cursor.fetchall()    
    list = []
    for row in data:
        mes = weather_measurement()
        mes.ambient_temperature = float(row[0])
        mes.ground_temperature = float(row[1])
        mes.air_quality = float(row[2])
        mes.humidity = float(row[3])
        mes.wind_speed = float(row[4])
        mes.wind_gust_speed = float(row[5])
        mes.rainfall = float(row[6])
        mes.created = str(row[7])
        mes.air_pressure = float(row[8])
        list.append(mes)
    conn.close()
    cursor.close()
    resp = Response(json.dumps(list, cls=MyEncoder), status=200, mimetype='application/json')
    resp.headers["Access-Control-Allow-Origin"] = '*'
    return resp
if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)

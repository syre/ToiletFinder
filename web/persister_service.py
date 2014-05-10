#!/usr/bin/python3

import mosquitto
import os
import json
import sqlite3
import time

DATABASE = 'toilets.sqlite3'
HOSTNAME = "127.0.0.1"
PORT = 1883

def connect_db():
    return sqlite3.connect(DATABASE)

def persist(mosq, obj, msg):
        message = json.loads(msg.payload.decode())
        print("%r" % message)
        if (message["occupied"]):
            message["occupied"] = 1
        else:
            message["occupied"] = 0
        connection = connect_db()
        c = connection.cursor()
        # persist data to events table
        c.execute("insert into events values (NULL,?,?,?,?,?)", [message["group_id"], message["toilet_id"], message["methane_level"], message["occupied"], message["time_stamp"]])
        # update specific toilet
        c.execute("update toilets set occupied=?, methane_level=? where id=?", [message["occupied"], message["methane_level"], message["toilet_id"]])
        connection.commit()
        connection.close()


mypid = os.getpid()
mqttc = mosquitto.Mosquitto(str(mypid))
mqttc.connect(HOSTNAME, PORT, 60, True)

mqttc.on_message = persist
mqttc.subscribe("persist", 0)

while(mqttc.loop() == 0):
    pass

mqttc.disconnect()
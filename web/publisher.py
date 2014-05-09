#!/usr/bin/python3

import pika
import json
import datetime

cred = pika.PlainCredentials("node", "node")
connection = pika.BlockingConnection(pika.ConnectionParameters("127.0.0.1",credentials=cred))

channel = connection.channel()
channel.exchange_declare(exchange="toilet_events", type="fanout")
message = {"group_id":1, "toilet_id": 2, "occupied":False, "methane_level": 20, "time_stamp": str(datetime.datetime.now())}

channel.basic_publish(exchange="toilet_events",routing_key="", body=json.dumps(message))

connection.close()

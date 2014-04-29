#!/usr/bin/python3

import pika
import json
import sqlite3
import time

DATABASE = 'toilets.sqlite3'

def connect_db():
    return sqlite3.connect(DATABASE)


cred = pika.PlainCredentials("node", "node")
connection = pika.BlockingConnection(pika.ConnectionParameters("127.0.0.1",credentials=cred))

channel = connection.channel()
channel.exchange_declare(exchange="logs", type="fanout")
result = channel.queue_declare()

queue_name = result.method.queue

channel.queue_bind(exchange="logs", queue=queue_name)
def callback(ch, method, properties, body):
        message = json.loads(body.decode())
        print("%r" % message)
        if (message["occupied"]):
            message["occupied"] = 1
        else:
            message["occupied"] = 0
        connection = connect_db()
        c = connection.cursor()
        c.execute("insert into events values (NULL,?,?,?,?,?)", [message["group_id"], message["toilet_id"], message["methane_level"], message["occupied"], message["time_stamp"]])
        connection.commit()
        connection.close()

channel.basic_consume(callback, queue=queue_name, no_ack=True)
channel.start_consuming()
connection.close()
close_connection()

#!/usr/bin/python3
import flask
import sqlite3

DATABASE = 'toilets.sqlite3'

app = flask.Flask(__name__)

def connect_db():
	return sqlite3.connect(DATABASE)

def get_db():
	db = getattr(flask.g, '_database', None)
	if db is None:
		db = flask.g._database = connect_db()
	return db

def query_db(query, args=(), one=False):
    cur = get_db().execute(query, args)
    rv = cur.fetchall()
    cur.close()
    return (rv[0] if rv else None) if one else rv

@app.teardown_appcontext
def close_connection(exception):
	db = getattr(flask.g, '_database', None)
	if db is not None:
		db.close()

@app.route('/toiletgroups/')
def index():
    group_result = query_db('select * from toilet_group')
    group_arguments = ["id", "name", "lat", "lng"]
    toilet_arguments = ['id', 'group_id', 'description', 'location', 'occupied', 'methane_level', 'lat', "lng"]
    list = []
    groupdict = {}
    for group in group_result:
        groupdict = dict(zip(group_arguments, group))
        toilet_result = query_db('select * from toilets where group_id == ?', [group[0]])
        groupdict['toilets'] = []
        for toilet in toilet_result:
            groupdict['toilets'].append(dict(zip(toilet_arguments, toilet)))
    list.append(groupdict)
    return flask.jsonify(groups = list)

@app.route("/toiletgroups/<id>")
def indexSpecific(id=None):
    id = int(id)
    group_result = query_db('select * from toilet_group where id == '+str(id))
    group_arguments = ["id", "name", "lat", "lng"]
    toilet_arguments = ['id', 'group_id', 'description', 'location', 'occupied', 'methane_level', 'lat', "lng"]
    list = []
    groupdict = {}
    for group in group_result:
        groupdict = dict(zip(group_arguments, group))
        toilet_result = query_db('select * from toilets where group_id == ?', [group[0]])
        groupdict['toilets'] = []
        for toilet in toilet_result:
            groupdict['toilets'].append(dict(zip(toilet_arguments, toilet)))
    list.append(groupdict)
    return flask.jsonify(groups = list)

if __name__ == '__main__':
	app.run(host="0.0.0.0", debug = True, port=5000)
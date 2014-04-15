#!flask/bin/python
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

@app.route('/toilets/')
def index():
	result = query_db('select * from toilets')
	arguments = ['id', 'name', 'lat', 'lng', 'occupied', 'description']
	list = []
	for toilet in result:
		list.append(dict(zip(arguments, toilet)))
	return flask.jsonify(results = list)

if __name__ == '__main__':
	app.run(debug = True)
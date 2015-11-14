# To run: SPARK_CLASSPATH=./mysql-connector-java-5.1.37/mysql-connector-java-5.1.37-bin.jar ./spark-1.5.1-bin-hadoop2.4/bin/pyspark Spark/flow.py

import sys
from database import *
from utils.user import *
from pyspark import SparkContext
from pyspark.sql import SQLContext
from scipy.spatial import KDTree
from feature_set import *
from decision_tree import *

DEBUG = True

# Initialize sqlContext
sc = SparkContext()
sqlContext = SQLContext(sc)


# Load data from database
users = load_db_table(sqlContext, 'user').select('user_id', 'birthyear', 'gender', 'timezone')
users.printSchema()

event_attendees = load_db_table(sqlContext, 'event_attendees')
event_attendees.printSchema()
event_attendees.registerTempTable('event_attendees')

users = users.filter(users['user_id'] > 0)

# Construct KD tree
sys.setrecursionlimit(10000)  # Prevent recursion limit Error
data = []
user_ids = []
for user in users.collect():
	data.append(get_user_numeric_vector(user))
	user_ids.append(user.user_id)
tree = KDTree(data)

with open('output.txt', 'w') as f:
	# f.write('users.count() = {}\n'.format(users.count()))
	for uid, user in enumerate(data):
		neigh = tree.query(user, k=1000)

		if DEBUG:
			f.write('{}\n'.format(users.collect()[uid].user_id))

			neigh_id = []
			for nid in neigh[1]:
				neigh_id.append(str(user_ids[nid]))

			data = featureExtract(neigh_id)[0]
			n_record = len(data[0])
			features = data[0]
			interested = data[1]
			notinterested = data[2]
			keys = data[3]

			model = build_decision_tree(sqlContext, features, interested)
		else:
			f.write('neigh = {}\n'.format(neigh[1]))

		if uid == 0:
			break

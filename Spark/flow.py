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

n_experiment = 0
sum_events = 0
sum_error = 0
sum_recommended = 0
with open('output.txt', 'w') as f:
	# f.write('users.count() = {}\n'.format(users.count()))
	K = 1000
	f.write('k = {}\n'.format(K))
	for uid, user in enumerate(data):
		neigh = tree.query(user, k=K)

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

			f.write('# Events = {}\n'.format(len(interested)))
			error, model = build_decision_tree(sqlContext, features, interested)
			f.write("Error = {}\n".format(error))

			n_experiment += 1
			sum_error += error
			sum_events += len(interested)

			recommend_data = featureExtract(neigh_id, user_ids[uid])[0]
			print '--------------------'
			features = recommend_data[0]
			interested = recommend_data[1]
			keys = recommend_data[3]
			print '# events = ', len(interested)
			recommend_data = sqlContext.createDataFrame(
					[Row(label=interested[i], features=Vectors.dense(features[i])) for i in xrange(len(features))])
			recommend_data.printSchema()
			predictions = model.transform(recommend_data).select("prediction")

			index = 0
			recommended_events = []
			for row in predictions.collect():
				if row.prediction > 0.5:
					recommended_events.append(keys[index][1])
				index += 1

			print 'User: ', user_ids[uid]
			print 'Recommended events: ({})'.format(','.join(map(str, recommended_events)))
			print 'Recommended: {} events from {} candidates'.format(len(recommended_events), len(interested))

			sum_recommended += len(recommended_events)
		else:
			f.write('neigh = {}\n'.format(neigh[1]))

		if uid == 3:
			break
	
	f.write('-----------------------\n')
	f.write('# exp    = {}\n'.format(n_experiment))
	f.write('# events = {}\n'.format(sum_events * 1.0 / n_experiment))
	f.write('errors   = {}\n'.format(sum_error * 1.0 / n_experiment))
	f.write('# rec    = {}\n'.format(sum_recommended * 1.0 / n_experiment))

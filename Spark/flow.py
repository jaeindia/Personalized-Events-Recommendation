# To run: SPARK_CLASSPATH=./mysql-connector-java-5.1.37/mysql-connector-java-5.1.37-bin.jar ./spark-1.5.1-bin-hadoop2.4/bin/pyspark Spark/flow.py

import sys
from database import *
from utils.user import *
from pyspark import SparkContext
from pyspark.sql import SQLContext
from scipy.spatial import KDTree

DEBUG = False

# Initialize sqlContext
sc = SparkContext()
sqlContext = SQLContext(sc)


# Load data from database
users = load_db_table(sqlContext, 'user').select('user_id', 'birthyear', 'gender', 'timezone')
users.printSchema()

users = users.filter(users['user_id'] > 0)


# Construct KD tree
sys.setrecursionlimit(10000)  # Prevent recursion limit Error
data = []
for user in users.collect():
    data.append(get_user_numeric_vector(user))
tree = KDTree(data)

with open('output.txt', 'w') as f:
    f.write('users.count() = {}\n'.format(users.count()))
    for uid, user in enumerate(data):
        neigh = tree.query(user, k=3)

        if DEBUG:
            f.write(' user = {}\n'.format(users.collect()[uid]))
            
            for nid in neigh[1]:
                f.write('neigh = {}\n'.format(users.collect()[nid]))
            f.write('\n')
        else:
            f.write('neigh = {}\n'.format(neigh[1]))

        if uid == 10:
            break

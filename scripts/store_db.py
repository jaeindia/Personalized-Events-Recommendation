import sys
import pymongo
from pymongo import MongoClient

print 'Connecting to database..'
client = MongoClient()

DB_NAME = 'cs5228'
TABLE_NAME = sys.argv[2]
FILE_PATH = sys.argv[1]
N_ROW = 1000

print 'Reading file {} to table {}'.format(FILE_PATH, TABLE_NAME)

db = client[DB_NAME]
table = db[TABLE_NAME]

# Clear all existing rows!
table.remove()

with open(FILE_PATH, 'r') as f:
    row_id = 0
    fields = []

    # For each line in the file
    for line in f:
        row_id += 1
        if row_id == 1:
            # This is header column --> get field name from this line
            fields = line.split(',')
            fields = map(str.strip, fields)
        else:
            values = line.split(',')
            values = map(str.strip, values)

            # Construct the object to store in mongo DB
            current_obj = {}
            for i in xrange(len(fields)):
                current_obj[ fields[i] ] = values[i]

            table.insert_one(current_obj)

    print fields

import MySQLdb
import unicodedata
from datetime import datetime
import pandas as pdRef
import pprint

# DB connect
def DBConnect():
    global db
    global cur
    global queryStr
    
    db = MySQLdb.connect(host="localhost",  # localhost
                        user="root",  # username
                        passwd="root",  # password
                        db="eventsrecommendation")  # data base    
    cur = db.cursor(MySQLdb.cursors.DictCursor) 


# Cache for function results
def memoize(function):
    cache = {}

    def helper(*args):
        if args not in cache:
            cache[args] = function(*args)
        
        return cache[args]

    return helper


# Normalize using NFKD
def normalizeCaseless(text):
    return unicodedata.normalize("NFKD", text.casefold())


# String comparison
def caselessEqual(left, right):
    return normalizeCaseless(left) == normalizeCaseless(right)


# Location Comparison
def compareLocation(loc1, loc2):
    if (caselessEqual(loc1, loc2)):
        return 1
    return 0


@memoize
def getUserAttendance(userid, eventid):
    queryStr = ("SELECT * "
             "FROM event_attendees "
             "WHERE user_id = %s "
             "AND event_id = %s"
    )
    
    cur.execute(queryStr, (userid, eventid))
    
    return cur.fetchone()


@memoize
def getEventAttendance(eventid):
    queryStr = ("SELECT * "
             "FROM featuregroup1 "
             "WHERE event_id = %s"
    )
    
    cur.execute(queryStr, (eventid,))
    
    return cur.fetchone()


@memoize
def getEventAttOfConnections(userid, eventid):
    queryStr = ("SELECT * "
             "FROM featuregroup2 "
             "WHERE event_id = %s "
             "AND user_id = %s"
    )
    
    cur.execute(queryStr, (eventid, userid))
    
    return cur.fetchone()


@memoize
def getFriendsCount(userid):
    queryStr = ("SELECT count(*) count "
             "FROM user_connections "
             "WHERE user_id = %s "
    )
    
    cur.execute(queryStr, (userid,))
    
    return cur.fetchone()


def getUserInfo(useridList):
    queryStr = ("SELECT * "
             "FROM user "
             "WHERE user_id in (%s)"
    )
    formatString = ','.join(['%s'] * len(useridList))
     
    cur.execute(queryStr % formatString, tuple(useridList))
      
    return cur.fetchall()


def ifNull(a, b):
    if a is None or a == 0:
        return b
    return a

def nvlAttendance():
    qryResult = {}
    qryResult['yes'] = 0
    qryResult['no'] = 0
    qryResult['maybe'] = 0
    qryResult['invited'] = 0
    
    return qryResult
    

def process(userid, eventDict):
    global qryResult
    featureDict = {}
# #     'user_choice'
#     getUserAttendance(userid, eventid)

#     print "userid %s \n" % (userid,)

    for eventid in eventDict.keys():
#         print "eventid %s \n" % (eventid,)
        qryResult = getEventAttendance(eventid)
        
        featureList = []
        
        if qryResult is None:
            qryResult = nvlAttendance()
        
        #     0 - users attending event
        #     1 - users not attending event
        #     2 - users maybe attending event
        #     3 - users invited to event
        #     4 - f1 / f0
        #     5 - f2 / f0
        #     6 - f3 / f0

        featureList.append(qryResult['yes'])
        featureList.append(qryResult['no'])
        featureList.append(qryResult['maybe'])
        featureList.append(qryResult['invited'])
        featureList.append(qryResult['no'] * 1.0 / ifNull(qryResult['yes'], 1))
        featureList.append(qryResult['maybe'] * 1.0 / qryResult['yes'])
        featureList.append(qryResult['invited'] * 1.0 / qryResult['yes'])
    

        #     7 - friends attending event
        #     8 - friends not attending event
        #     9 - friends maybe attending event
        #     10 - friends invited to event
        #     11 - f8 / f7
        #     12 - f9 / f7
        #     13 - f10 / f7
        #     14 - f7 / number of friends
        #     15 - f8 / number of friends
        #     16 - f9 / number of friends
        #     17 - f10 / number of friends    
    
        qryResult = getEventAttOfConnections(userid, eventid)
        if qryResult is None:
            qryResult = nvlAttendance()
        featureList.append(qryResult['yes'])
        featureList.append(qryResult['no'])
        featureList.append(qryResult['maybe'])
        featureList.append(qryResult['invited'])
        featureList.append(qryResult['no'] * 1.0 / ifNull(qryResult['yes'], 1.0))
        featureList.append(qryResult['maybe'] * 1.0 / ifNull(qryResult['yes'], 1.0))
        featureList.append(qryResult['invited'] * 1.0 / ifNull(qryResult['yes'], 1.0))   
        qryResultAlt = getFriendsCount(userid)
        if qryResultAlt is None:
            qryResultAlt['count'] = 0
        featureList.append(qryResult['yes'] * 1.0 / ifNull(qryResultAlt['count'], 1.0))
        featureList.append(qryResult['no'] * 1.0 / ifNull(qryResultAlt['count'], 1.0))
        featureList.append(qryResult['maybe'] * 1.0 / ifNull(qryResultAlt['count'], 1.0))
        featureList.append(qryResult['invited'] * 1.0 / ifNull(qryResultAlt['count'], 1.0))
            
        #     18 - location
        # Location feature - Have to work on it
        featureList.append(1.0)
        
        #     19 - age
        # add age profile
        featureList.append(1.0)
        
        #     20 - gender
        # add gender profile
        featureList.append(1.0)
        
        #     21 - similarity between user and event based on attendance
        
#         print "feature list\n"
#         pprint.pprint(featureList)
        
        featureDict[eventid] = featureList
    
    return featureDict


# For Test - KNN result will be used in the actual engine
def getFeatureSet():
    train = pdRef.read_csv("../Input/train2.csv")
    trainDict = {}
    useridList = []
    global userInfo
    userInfo = {}
    
    for record in train.iterrows():
        record = record[1]
        userid = record['user']
        if userid not in trainDict:
            trainDict[userid] = []
        trainDict[userid].append({
            'eid': record['event'],
            'invited': record['invited'],
            'interested': record['interested'],
            'not_interested': record['not_interested']
        })
        
        useridList.append(userid)
    
    userInfo = getUserInfo(useridList)
    
#     pprint.pprint(userInfo)
#     pprint.pprint(trainDict)

    featureSet = []
    interested = []
    notinterested = []
    
    for userid, events in trainDict.iteritems():
        eventDict = {e['eid']: e['invited'] for e in events}
        featureDict = {} 
        featureDict = process(userid, eventDict)
        
#         print "feature dict"
#         pprint.pprint(featureDict)
        
        for e in events:
            featureSet.append(featureDict[e['eid']])
            interested.append(e['interested'])
            notinterested.append(e['not_interested'])
        
    return (featureSet, interested, notinterested)



# Main Run
# Start Time
startTime = datetime.now()

DBConnect()

pprint.pprint(getFeatureSet())

# Print run time
print datetime.now() - startTime

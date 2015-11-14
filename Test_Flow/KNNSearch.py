'''
@author: Jayakumar
'''
import numpy as np
import MySQLdb
from sklearn.neighbors import NearestNeighbors
import pprint

# DB connect
def DBConnect():
    global cur
    
    db = MySQLdb.connect(host="localhost",  # localhost
                        user="root",  # username
                        passwd="root",  # password
                        db="eventsrecommendation")  # data base    
    cur = db.cursor(MySQLdb.cursors.DictCursor)


def getUserInfo():
    queryStr = ("SELECT * "
             "FROM user "
             "ORDER BY user_id"
    )
     
    cur.execute(queryStr)
      
    return cur.fetchall()


def getBirthYearAvg():
    queryStr = ("SELECT round(avg(birthyear)) "
             "FROM user "
             "WHERE birthyear != 0"
    )
     
    cur.execute(queryStr)
      
    return cur.fetchone()


def getUserNumericVector(user):
    gender = 2
    if user['gender'] == 'female':
        gender = 0
    else:
        gender = 1
    return np.array([
        (user['birthyear'] - 1992) / 2.0,
        user['timezone'] / 400.0,
        gender,
        hash('locale')
    ])
    


def Search(userVector, k):    
    DBConnect()
    
    data = []
    
    userInfo = getUserInfo()
    
    for user in userInfo:
        data.append(getUserNumericVector(user)) 
    
#     userVector = data[0]
#     print "data\n"
#     pprint.pprint(userInfo[0])
#     pprint.pprint(data[0])
#     print "\n"
    
    nbrs = NearestNeighbors(n_neighbors=k, algorithm='kd_tree').fit(data)
    distances, indices = nbrs.kneighbors(userVector)
    
#     print "distances\n"
#     pprint.pprint(distances)
#     print"\n"
    
#     print "indices\n"
#     pprint.pprint(indices)
    nbrUserids = []
    print "\n"
    
    for index in indices[0]:
        nbrUserids.append(userInfo[index]['user_id'])
    
#     print "nbrUserids\n"
#     print nbrUserids
#     print "\n"
#     print len(nbrUserids)
    
    # Flush
    distances = None
    indices = None
    userInfo = None

    return nbrUserids


# user_id = 6110    
# Search([ 1.0 , -1.2,  1.0 ])
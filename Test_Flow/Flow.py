'''
@author: Jayakumar
'''
import KNNSearch
import FeatureSet
from Model import Model
import pprint
import pandas as pd
import sys

def getTestData():
    test = pd.read_csv("../Input/train2.csv")
    testDict = {}
    
    for record in test.iterrows():
        record = record[1]
        uid = record['user']
        if uid not in testDict:
            testDict[uid] = []
        testDict[uid].append({
            'eid': record['event'],
            'invited': record['invited']
        })
        
    testData = {}
    for uid, events in testDict.iteritems():
        eDict = {e['eid']: (e['invited']) for e in events}
        features_dict = FeatureSet.process(uid, eDict)
        X = []
        for e in events:
            eid = e['eid']
            X.append(features_dict[eid])
        testData[uid] = { 
            'X': X,
            'events': events
        }
        
    return testData


def runModel(model, testData):
    results = {}
    for uid, record in testData.iteritems():
        X = record['X']
        events = record['events']
        Y1 = model.test(X)
        final = Y1
        sortedEvents = []
        for i, e in enumerate(events):
            score = final[i]
            sortedEvents.append((e['eid'], score))
        sortedEvents.sort(key=lambda x: -x[1])
        sortedEvents = [e[0] for e in sortedEvents]
        results[uid] = sortedEvents
        
    return results
        

def runFull():
    nbrUserids = KNNSearch.Search([ 1.0 , -1.2,  1.0, 7.79 ], 2000)
    
#     print "Neighbours\n"
#     pprint.pprint(nbrUserids)
    
    split = FeatureSet.featureExtract(nbrUserids)
    
#     print "split\n"
#     pprint.pprint(split)
    
#     testData = getTestData()
#     print "Test Data\n"
#     pprint.pprint(testData);
#     
#     sys.exit(0)
    
    featureSet = split[0][0]
    interested = split[0][1]
    notinterested = split[0][2]
    
    z = [True] * len(featureSet[0])
    w = [True] * len(featureSet[0])
    
    C = 0.03
    #C = 0.3
    model = Model(compress=z, has_none=w, C=C)
    model.fit(featureSet, interested)
    
    testData = getTestData()
    
    result = runModel(model, testData)
    
    print result
    

runFull()
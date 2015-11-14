'''
@author: Jayakumar
'''

from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
import numpy as np
        
class Model:
    def __init__(self, compress=None, has_none=None, C=0.03, n_est=300):
        print C
        self.models = [
            RandomForestClassifier(n_estimators=n_est),
            LogisticRegression(C=C, penalty='l1')
        ]
        self.compress = compress
        self.has_none = has_none
    
    def fit(self, X, Y):
        X2 = np.array(X)
        if self.compress:
            X3 = X2.compress(self.compress, axis=1)
        self.models[0].fit(X3, Y)
        if self.has_none:
            X3 = X2.compress(self.has_none, axis=1)
        self.models[1].fit(X3, Y)
        
    def test(self, X):
        X2 = np.array(X)
        if self.compress:
            X3 = X2.compress(self.compress, axis=1)
            
        rez = self.models[0].predict_proba(X3)[:,1] * 0.69
        
        if self.has_none:
            X3 = X2.compress(self.has_none, axis=1)
        rez += self.models[1].predict_proba(X3)[:,1] * 0.57
        
        return rez
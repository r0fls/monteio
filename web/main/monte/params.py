from datetime import datetime
from pandas.io.data import DataReader
from numpy import log, std
from scipy.stats import laplace


def getParams(ticker):
    data = DataReader(ticker, "yahoo", datetime(1890, 1, 1), datetime.now())
    changes = log(data['Adj Close'][1:]) - log(data.shift()['Adj Close'][1:])
    params = laplace.fit(changes)
    return params, std(changes[-365:]) * (365**(.5))

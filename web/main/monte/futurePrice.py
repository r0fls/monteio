from params import getParams
from scipy.stats import laplace
from ystockquote import get_price 
from operator import mul
from numpy import exp
from bs import BlackScholes

class futurePrice():
    def __init__(self,ticker):
        self.ticker = ticker
        params = getParams(ticker)
        self.mean = params[0][0]
        self.var = params[0][1]
        self.vol = params[1]
        self.price = get_price(self.ticker)
    def futurePrice(self,days,strike,flag='C'):
        changes = laplace.rvs(0,self.var,size=days)
        values = exp(changes)
        self.daily = [float(self.price)*prod(values[0:i+1]) for i in range(len(values))]
        self.bs = [BlackScholes(flag,float(price),float(strike),.005,self.vol,float(days-i)/365) for i,price in enumerate(self.daily)]
        #self.bs = [BlackScholes(flag,float(price),float(strike),.005,self.vol,float(days)/365) for price in self.daily]
def prod(iterable):
        return reduce(mul, iterable, 1)

#functional version for parallel proccessing
def price(futurePrice,days,strike,flag='C'):
    changes = laplace.rvs(0,futurePrice.var,size=days)
    values = exp(changes)
    daily = [float(futurePrice.price)*prod(values[0:i+1]) for i in range(len(values))]
    bs = [BlackScholes(flag,float(price),strike,.005,futurePrice.vol,float(days-i)/365) for i,price in enumerate(daily)]
    return sum(bs)/len(bs)


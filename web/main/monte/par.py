import futurePrice
from multiprocessing import Pool

# Iterations for Monte carlo simulation

def parPrice(i):
    return futurePrice.price(*i)

def price(f,days,strike, putcall, iterations):
    p = Pool(4)
    return(sum(p.map(parPrice,[(f, days, strike, putcall[0]) for i in range(iterations)]))/iterations)
 

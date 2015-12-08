import time
from monte.futurePrice import futurePrice, price
from multiprocessing import Pool


TICKER = 'GOOGL'
ITERATIONS = 200
RUNS = 5

f = futurePrice(TICKER)

def parPrice(i):
    return price(*i)

def par():
    p = Pool(4)
    print(sum(p.map(parPrice,[(f, 100,780) for i in range(ITERATIONS)]))/ITERATIONS)

def loop():
    x = 0
    for i in range(ITERATIONS):
        x = x + price(f, 100, 780)
    print(x/ITERATIONS)


def gen():
    print(sum([price(f,100,780) for i in range(ITERATIONS)])/ITERATIONS)


def times(func):
    t0 = time.time()
    for i in range(RUNS): 
        func()
    t1 = time.time()
    return (t1-t0)/RUNS


if __name__=="__main__":
    print('average time for {0}:{1}s'.format('par',times(par)))
#    print('average time for {0}:{1}s'.format('gen',times(gen)))
#    print('average time for {0}:{1}s'.format('loop',times(loop)))


from scipy.stats import norm
from numpy import mean, std, linspace
from matplotlib.pyplot import plot, show,hist,grid,legend,xticks,plot

n = 5000

data = norm.rvs(loc=165, scale=1, size=1000)
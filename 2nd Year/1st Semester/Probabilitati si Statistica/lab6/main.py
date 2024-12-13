from scipy.stats import norm
from numpy import mean, std, linspace
from matplotlib.pyplot import plot, show,hist,grid,legend,xticks,plot

data = norm.rvs(loc=0, scale=1, size=1000)
# #1
# I

from scipy.stats import norm
from numpy import mean, std, linspace
from matplotlib.pyplot import plot, show, hist, grid, legend, xticks, plot
#
n = 5000
#
data = norm.rvs(loc=165, scale=10, size=n)
#
# hist(data, bins=14, density=True, range=(130, 200), label='frecvente relative')
#
# x = linspace(130, 200, 10000)
# plot(x, norm.pdf(x, loc=165, scale=10), 'r-', label='functia de densitate')
#
# xticks(range(130, 200, 5))
# legend(loc='upper right')
# grid()
# show()


#II

print(data.mean())
print(mean(data))
print(data.std())

print(sum((160<=data)&(data<=170))/n)
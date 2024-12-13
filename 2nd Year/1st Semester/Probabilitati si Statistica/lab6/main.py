# #1
# I
from PIL.ImageOps import scale
from scipy.stats import norm
from numpy import mean, std, linspace
from matplotlib.pyplot import plot, show, hist, grid, legend, xticks, plot

#
# n = 5000
#
# data = norm.rvs(loc=165, scale=10, size=n)
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


# II
#
# print(data.mean())
# print(mean(data))
# print(data.std())
#
# print(sum((160 <= data) & (data <= 170)) / n)
#
# norm.cdf(170, loc=165, scale=10) - norm.cdf(160, loc=165, scale=10)


#2

from scipy.stats import expon, uniform
from numpy import mean, std, multiply

n = 5000
r = uniform.rvs(size=n)
data = expon.rvs(loc=0, scale=5, size=n)*(r<0.4) + uniform.rvs(loc=1, scale=2, size=n)*(r>=0.4)
print(data)
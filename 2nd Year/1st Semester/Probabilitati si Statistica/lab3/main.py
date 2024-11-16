from random import randrange

from matplotlib.pyplot import bar, hist, grid, show, legend
from scipy.stats import binom

# data = [randrange(1, 7) for _ in range(500)]
# bin_edges = [k + 0.5 for k in range(7)]
# hist(data, bin_edges, density = True, rwidth = 0.9, color = 'green', edgecolor = 'black',
# alpha = 0.5, label = 'frecvente relative')
# distribution = dict([(i, 1/6) for i in range(1, 7)])
# bar(distribution.keys(), distribution.values(), width = 0.85, color = 'red', edgecolor = 'black',
# alpha = 0.6, label = 'probabilitati')
# legend(loc='lower left')
# grid()
# show()


#3) a)
# n, p = 5, 0.6
#
# x = binom.rvs(n, p, size=1000)
#
# print(x)

#3) b)
# data = x
# bin_edges = [k + 0.5 for k in range(6)]
# hist(data, bin_edges, density = True, rwidth = 0.9, color = 'green', edgecolor = 'black',
# alpha = 0.5, label = 'frecvente relative')
# distribution = dict([(i, binom.pmf(i, n, p)) for i in range(6)])
# bar(distribution.keys(), distribution.values(), width = 0.85, color = 'red', edgecolor = 'black',
# alpha = 0.6, label = 'probabilitati')
# legend(loc='lower left')
# grid()
# show()


# #3) c)
# y = binom.cdf(5, n, p) - binom.cdf(2, n, p)
#
# print(y)


from matplotlib import pyplot as plt
from itertools import product
from collections import Counter

# Generarea tuturor combinațiilor posibile de aruncări
all_combinations = list(product(range(1, 7), repeat=3))
sum_counts = Counter([sum(comb) for comb in all_combinations])

# Calcularea probabilităților teoretice
total_combinations = len(all_combinations)
probabilities = {s: count / total_combinations for s, count in sum_counts.items()}

# Afișarea barelor probabilităților
plt.bar(probabilities.keys(), probabilities.values(), width=0.85, color='red', edgecolor='black', alpha=0.6, label='Probabilități teoretice')
plt.xlabel('Suma zarurilor')
plt.ylabel('Probabilitate')
plt.title('Probabilitățile teoretice ale sumelor zarurilor')
plt.legend(loc='upper right')
plt.grid()
plt.show()
from scipy.stats import hypergeom, geom

p = sum([hypergeom.pmf(k, 49, 6, 6) for k in range(3, 7)])

nr_simulations = 1000

nr_bilete_necastigatoare = geom.rvs(p, size=nr_simulations)

print(nr_bilete_necastigatoare)


# X >= 10
nr_bilete_necastigatoare_mai_mare_de_10 = sum([1 for x in nr_bilete_necastigatoare if x >= 10])

print(nr_bilete_necastigatoare_mai_mare_de_10 / nr_simulations)


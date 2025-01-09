from random import sample
from math import factorial
from itertools import permutations

# 2
# a
cuvant = 'word'
permutari = [''.join(p) for p in permutations(cuvant)]
print(permutari)
print(list(permutations(cuvant)))
print(list(permutations([1, 3, 4])))

# b
print(len(permutari))
print(factorial(len('word')))

# c
print(sample(permutari, 1))
print('\n\n')

# 3
from math import perm, comb
from itertools import combinations


# a
def aranjamente(lista, k, numar_total=False, aleator=False):
    """Aranjamentele listei, numarul lor total si generare de un aranjament aleator."""
    if numar_total:
        print(perm(len(lista), k))
    elif aleator:
        print(sample(lista, k))
    else:
        print(list(permutations(lista, k)))


aranjamente('word', 2)
aranjamente('word', 2, numar_total=True)
aranjamente('word', 2, aleator=True)

print('\n')

def combinari(lista, k, numar_total=False, aleator=False):
    """Combinarile listei, numar lor totalu si generare de o combinare aleatoare."""
    if numar_total:
        print(comb(len(lista), k))
    elif aleator:
        indici_aleatori = sample(list(range(len(lista))), k)
        print([lista[i] for i in sorted(indici_aleatori)])
    else:
        print(list(combinations(lista, k)))


combinari('word', 2)
combinari('word', 2, numar_total=True)
combinari('word', 2, aleator=True)


# 4
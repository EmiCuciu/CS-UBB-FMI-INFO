from itertools import permutations, combinations
from random import sample


# print(list(permutations('word', 4)))

# print(factorial(len('word')))

# print(sample(list('word'), 4))

def aranjamente(cuvant, lungime, numar_total=False, aleator=False):
    if numar_total:
        return list(permutations(cuvant, lungime))
    elif aleator:
        b = sample(list(cuvant), lungime)
        return b
    else:
        a = permutations(cuvant, lungime)
        return len(list(a))


cuv = 'word'
lung = 2


# print(arangamente(cuvant, lungime, True, False))
# print(arangamente(cuvant, lungime, False, False))
# print(arangamente(cuvant, lungime, False, True))

def combinari(cuvant, lungime, numar_total=False, aleator=False):
    if numar_total:
        return list(combinations(cuvant, lungime))
    elif aleator:
        b = sample(list(cuvant), lungime)
        return b
    else:
        a = combinations(cuvant, lungime)
        return len(list(a))


# print(combinari(cuv, lung, True, False))
# print(combinari(cuv, lung, False, False))
# print(combinari(cuv, lung, False, True))

print(len(list(aranjamente('1' * 8, 5, True, False))))



#se citesc elementele x1,x2,...,xn

#solutie candidat: x=(x1,x2,...,xk), x apartine (0,1);
# Fiecare xi reprezintă un operator (+ sau -) între numerele corespunzătoare din listă.


#conditie consistent: x=(x1,x2,...,xk), este consistent daca xi-1 != xi pentru orice i, iar k<=n unde n=lungimea listei

#conditie solutie: x=(x1,x2,...,xk) este o soluție dacă este consistent si rezultatul expresiei este pozitiv, unde n este lungimea listei de numere

# conditie


def solutie(x, l):
    suma_totala = 0
    if len(x) == len(l):
        for i in range(len(l)):
            suma_totala += l[i] * x[i]
        return suma_totala > 0
    return False


def consistent(x, l):
    return len(x) <= len(l)


def afisare_solutie(x, l):
    string_solutie = ""
    for i in range(len(x)):
        if x[i] == 1:
            string_solutie += "+"
        else:
            string_solutie += "-"
        string_solutie += str(l[i])
    print(string_solutie)

def back_rec(x, l):
    x.append(1)  # adaugă o nouă componentă la soluția candidată
    for i in [-1, 1]:
        x[-1] = i  # setează componenta curentă
        if consistent(x, l):
            if solutie(x, l):
                afisare_solutie(x, l)
            back_rec(x, l)  # invocare recursivă pentru a trata următoarele componente
    x.pop()
def back_iter(l):
    n = len(l)
    x = [-1] * n  # soluție candidată
    while True:
        # Afișează soluția curentă
        if solutie(x, l):
            afisare_solutie(x, l)

        # Găsește următoarea soluție
        i = 0
        while i < n and x[i] == 1:
            x[i] = -1
            i += 1
        if i == n:
            break
        x[i] = 1

if __name__ == "__main__":
    print('Recursiv')
    back_rec([], [1, 2, 3, 4])
    print('Iterativ')
    back_iter([1, 2, 3, 4])











#def solve_recursive(numbers, current_expression, target, current_sum, index, result):
#    if index == len(numbers):
#        if current_sum > 0:
#            result.append(current_expression)
#        return
#    # Adaugă operatorul +
#    solve_recursive(numbers, current_expression + '+' + str(numbers[index]), target, current_sum + numbers[index], index + 1, result)
#    # Adaugă operatorul -
#    solve_recursive(numbers, current_expression + '-' + str(numbers[index]), target, current_sum - numbers[index], index + 1, result)
#def solve_iterative(numbers, target):
#    result = []
#    stack = [(0, '', 0, 0)]  # (index, current_expression, current_sum, last_number)
#    while stack:
#        index, current_expression, current_sum, last_number = stack.pop()
#        if index == len(numbers):
#            if current_sum > 0:
#                result.append(current_expression)
#            continue
#        # Adaugă operatorul +
#        stack.append((index + 1, current_expression + '+' + str(numbers[index]), current_sum + numbers[index], numbers[index]))
#        # Adaugă operatorul -
#        stack.append((index + 1, current_expression + '-' + str(numbers[index]), current_sum - numbers[index], -numbers[index]))
#    return result
## Citirea listei de numere de la tastatură
#numbers = [int(x) for x in input("Introduceti lista de numere, separate prin spatiu: ").split()]
## Citirea țintei de la tastatură
#target = 0  # Setăm la 0 pentru a căuta combinații pozitive
#result_recursive = []
#solve_recursive(numbers, str(numbers[0]), target, numbers[0], 1, result_recursive)
#print("Variantele recursive:", result_recursive)
#result_iterative = solve_iterative(numbers, target)
#print("Variantele iterative:", result_iterative)

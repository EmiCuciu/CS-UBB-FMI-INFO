#include <stdio.h>

void suma(int n) {
    /*
     * Functia primeste ca parametru un numar natural n
     * si afiseaza toate modurile in care n poate fi scris ca suma de numere consecutive.
     * param n: integer; numarul dat
     * pre-conditii: n > 0
     * post-conditii: se afiseaza toate modurile in care n poate fi scris ca suma de numere consecutive
    */
    int i, j, suma, contor;

    for (i = 1; i <= n; i++) {
        suma = 0;
        contor = 0;
        for (j = i; j <= n; j++) {
            suma += j;
            contor++;
            if (suma == n) {
                printf("%d =", suma);
                for (int k = i; k <= j; k++) {
                    printf(" %d", k);
                    if (k != j) {
                        printf(" +");
                    }
                }
                printf("\n");
            }
        }
    }
}

void consecutive(int n) {
    /*
     * Functia primeste ca parametru un numar natural n , iar daca n este mai mare strict decat 0 atunci apeleaza functia suma(n)
     * param n: integer; numarul dat
     * pre-conditii: n > 0
     * post-conditii: daca n>0 atunci se apeleaza functia suma(n), altfel se afiseaza un mesaj de eroare
     */
    if (n < 1) {
        printf("Numarul trebuie sa fie mai mare strict decat 0\n");
        return;
    } else {
        printf("Reprezentarile lui %d ca suma de numere consecutive sunt:\n", n);
        suma(n);
    }
}

int main() {
    /*
     * Functia principala a programului, avem meniu cu doua optiuni: 1. Reprezentare si 0. Iesire,
     * in functie de optiunea aleasa se apeleaza functia consecutive(n) sau se iese din program
     * pre-conditii: -
     * post-conditii: se afiseaza meniul, conform optiunii alese se apeleaza functia consecutive(n) sau se iese din program
     */
    int n;
    int a = 1;
    int b;
// while (a != 0) {
//     printf("Alegeti optiunea:\n");
//     printf("1. Reprezentare\n");
//     printf("0. Iesire\n");
//     scanf("%d", &b);
//     if (b == 1) {
//         printf("Introduceti un numar: ");
//         scanf("%d", &n);
//         consecutive(n);
//     } else if (b == 0) {
//         printf("Salut!");
//         a = 0;
//     } else {
//         printf("Optiune invalida\n");
//     }
// }

//aplicatie de tip consola care face actiuni repetate pana la intalnirea lui 0 folosind switch
    while (a != 0) {
        printf("Alegeti optiunea:\n");
        printf("1. Reprezentare\n");
        printf("0. Iesire\n");
        scanf("%d", &b);
        switch(b){
            case 1:
                printf("Introduceti un numar: ");
                scanf("%d", &n);
                consecutive(n);
                break;
            case 0:
                printf("Salut!");
                a = 0;
                break;
            default:
                printf("Optiune invalida\n");
        }
    }

    return 0;
}
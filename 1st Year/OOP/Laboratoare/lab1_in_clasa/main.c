//12. Determina valoarea x^n (x este un numar real dat, n este un numar natural
//dat), utilizand operatii de inmultire si de ridicare la patrat.

#include <stdio.h>
/*
     * Functia calculeaza x la puterea n
     * :param x: numarul real
     * :param n: numarul natural
     * preconditii: x este un numar real, n este un numar natural>0
     * postconditii: se returneaza x la puterea n
     */
float power(float x, int n) {

    float result = 1;
    if (n == 2) {
        return x * x;
    } else if (n == 1) {
        return x;
    } else if (n > 2) {
        for (int i = 0; i < n; i++) {
            result *= x;        // inmultirea lui x de n ori
        }
        return result;      // returneaza rezultatul
    }
    return 0;
}


int main() {
    /*
     * Functia citeste de la tastatura un numar real x si un numar natural n si afiseaza x la puterea n
     * :return: 0
     */
    float x;
    int n;
    int a = 1, b = 1;
    while (a == 1) {
        printf("MENIU: \n");
        printf("1. Ridicare la putere \n");
        printf("0. Iesire \n");
        printf("Alegeti optiunea: ");
        scanf("%d", &b);
        if (b == 1) {
            printf("Enter x: ");
            scanf("%f", &x);
            printf("Enter n: ");
            scanf("%d", &n);
            if (n == 0) {
                printf("x^n = 1 \n");
                b=0;
            } else if (n > 0) {
                printf("%.2f^%d = %.2f\n", x, n, power(x, n));
                b=0;
            } else if (n < 0) {
                printf("n trebuie sa fie un numar natural!\n");
                b = 0;
            }
        } else if (b == 0) {
            printf("CEAW!!!\n");
            a = 0;
        }
        else {
            printf("Optiune invalida!\n");
        }
    }
    return 0;
}

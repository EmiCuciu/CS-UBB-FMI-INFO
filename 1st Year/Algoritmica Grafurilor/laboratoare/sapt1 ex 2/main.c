#include <stdio.h>

#define MAX_NODES 100

// Funcție pentru citirea matricei de adiacență din fișier
int citesteMatriceAdiacenta(FILE *fisier, int matrice[MAX_NODES][MAX_NODES], int *numarNoduri) {
    int i, j;

    if (fscanf(fisier, "%d", numarNoduri) != 1) {
        printf("Eroare la citirea numarului de noduri!\n");
        return 0;
    }

    for (i = 0; i < *numarNoduri; i++) {
        for (j = 0; j < *numarNoduri; j++) {
            if (fscanf(fisier, "%d", &matrice[i][j]) != 1) {
                printf("Eroare la citirea matricei de adiacenta!\n");
                return 0;
            }
        }
    }
    return 1;       // Citirea s-a efectuat cu succes
}

// Funcție pentru determinarea nodurilor izolate
void noduriIzolate(int matrice[MAX_NODES][MAX_NODES], int numarNoduri) {
    int i, j, izolat;
    int nodIzolatExista = 0; // Variabilă pentru a verifica dacă există noduri izolate

    printf("Noduri izolate: ");
    for (i = 0; i < numarNoduri; i++) {
        izolat = 1;     // Presupunem că nodul este izolat
        for (j = 0; j < numarNoduri; j++) {
            if (matrice[i][j]) { // Excluzând legătura cu nodul propriu
                izolat = 0;
                break;
            }
        }
        if (izolat) {
            printf("%d ", i+1);    // Afișăm nodul izolat
            nodIzolatExista++; // Setăm indicatorul că există cel puțin un nod izolat
        }
    }
    if (nodIzolatExista==0) {
        printf("Nu exista"); // Afișăm un mesaj dacă nu există noduri izolate
    }
    printf("\n");
}



// Funcție pentru determinarea dacă graf este regulat
int esteRegulat(int matrice[MAX_NODES][MAX_NODES], int numarNoduri) {
    /* Un graf este regulat dacă toate nodurile au același grad
     * Un nod are gradul egal cu numărul de muchii care sunt adiacente cu el
     * ex: pentru nodul i, gradul = suma elementelor de pe linia i din matricea de adiacență
     * Dacă toate nodurile au același grad, atunci graful este regulat
     */
    int i, j, grad;

    grad = 0;
    for (i = 0; i < numarNoduri; i++) {
        int gradNod = 0;    // Gradul nodului i
        for (j = 0; j < numarNoduri; j++) {
            gradNod += matrice[i][j];   // Gradul nodului i este suma elementelor de pe linia i
        }
        if (i == 0) {
            grad = gradNod;    // Inițializăm gradul cu gradul primului nod
        } else if (grad != gradNod) {
            return 0;   // Dacă gradul nodului i este diferit de gradul primului nod, atunci graful nu este regulat
        }
    }
    return 1;   // Toate nodurile au același grad
}

// Funcție pentru afișarea matricei de adiacență
void afiseazaMatriceAdiacenta(int matrice[MAX_NODES][MAX_NODES], int numarNoduri) {
    int i, j;

    printf("Matricea de adiacenta:\n");
    for (i = 0; i < numarNoduri; i++) {
        for (j = 0; j < numarNoduri; j++) {
            printf("%d ", matrice[i][j]);
        }
        printf("\n");
    }
}

// Funcție pentru determinarea matricei de distanțe
void matriceDistantelor(int matrice[MAX_NODES][MAX_NODES], int numarNoduri) {
    /* Matricea distantelor este o matrice patratica de dimensiunea numarNoduri x numarNoduri
     * unde elementul de pe linia i si coloana j reprezinta distanta minima dintre nodul i si nodul j
     * Daca nu exista drum intre nodul i si nodul j, atunci elementul de pe linia i si coloana j este 0
     */
    int i, j, k;
    int distante[MAX_NODES][MAX_NODES];   // Matricea distantelor

    for (i = 0; i < numarNoduri; i++) {
        for (j = 0; j < numarNoduri; j++) {
            distante[i][j] = matrice[i][j];   // Inițializăm matricea distantelor cu matricea de adiacență
        }
    }

    for (k = 0; k < numarNoduri; k++) {    // Parcurgem toate nodurile
        for (i = 0; i < numarNoduri; i++) {
            for (j = 0; j < numarNoduri; j++) {
                if (distante[i][k] && distante[k][j] && (distante[i][j] == 0 || distante[i][j] > distante[i][k] + distante[k][j])) {    // Dacă există un drum mai scurt
                    distante[i][j] = distante[i][k] + distante[k][j];   // Actualizăm distanța minimă
                }
            }
        }
    }

    printf("Matricea distantelor:\n");
    for (i = 0; i < numarNoduri; i++) {
        for (j = 0; j < numarNoduri; j++) {
            printf("%d ", distante[i][j]);
        }
        printf("\n");
    }
}

// Funcție pentru determinarea dacă graf este conex
int esteConex(int matrice[MAX_NODES][MAX_NODES], int numarNoduri) {
    /* Un graf este conex dacă există un drum între oricare două noduri
     * Putem folosi algoritmul lui Warshall pentru a determina dacă un graf este conex
     * Algoritmul lui Warshall este un algoritm pentru determinarea matricei tranzitivei a unei relații
     * Dacă matricea tranzitivă este plină, atunci graful este conex
     * Matricea tranzitivă este plină dacă pentru orice i, j, există un drum între nodul i și nodul j
     */
    int i, j, k;
    int vizitat[MAX_NODES] = {0};   // Vector pentru a marca nodurile vizitate

    vizitat[0] = 1;
    for (k = 0; k < numarNoduri; k++) {
        for (i = 0; i < numarNoduri; i++) {
            for (j = 0; j < numarNoduri; j++) {
                if ((matrice[i][j] || matrice[j][i]) && (vizitat[i] || vizitat[j])) {
                    // Dacă există un drum între nodul i și nodul j și unul dintre noduri a fost vizitat
                    vizitat[i] = 1;
                    vizitat[j] = 1;
                }
            }
        }
    }

    for (i = 0; i < numarNoduri; i++) {   // Verificăm dacă toate nodurile au fost vizitate
        if (vizitat[i] == 0) {
            return 0;
        }
    }
    return 1;
}

int main() {
    FILE *fisier;
    int matrice[MAX_NODES][MAX_NODES];
    int numarNoduri;

    fisier = fopen("graf.txt", "r");
    if (fisier == NULL) {
        // Eroare la deschiderea fișierului
        printf("Nu am putut deschide fisierul!\n");
        return 1;
    }

    if (!citesteMatriceAdiacenta(fisier, matrice, &numarNoduri)) {
        // Eroare la citirea matricei de adiacență
        fclose(fisier);
        return 1;
    }

    fclose(fisier);

    afiseazaMatriceAdiacenta(matrice, numarNoduri);

    noduriIzolate(matrice, numarNoduri);

    if (esteRegulat(matrice, numarNoduri)) {
        printf("Graful este regulat.\n");
    } else {
        printf("Graful nu este regulat.\n");
    }

    matriceDistantelor(matrice, numarNoduri);

    if (esteConex(matrice, numarNoduri)) {
        printf("Graful este conex.\n");
    } else {
        printf("Graful nu este conex.\n");
    }

    return 0;
}
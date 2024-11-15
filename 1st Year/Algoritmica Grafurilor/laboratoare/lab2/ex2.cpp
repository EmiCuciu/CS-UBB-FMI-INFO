/*
 * Sa se determine închiderea transitivă a unui graf orientat. (Închiderea tranzitivă poate fi reprezentată ca matricea care descrie,
 * pentru fiecare vârf în parte, care sunt vârfurile accesibile din acest vârf. Matricea inchiderii tranzitive arată unde
 * se poate ajunge din fiecare vârf.) ex. figura inchidere_tranzitiva.png - pentru graful de sus se construieste matricea de jos
 * care arata inchiderea tranzitiva.
 *
 *
 * Pentru un graf orientat, elementul (i, j) din matricea tranzitivă va fi 1 dacă există o cale de la nodul i la nodul j,
 * indiferent de lungimea acelei căi. Dacă nu există o cale de la i la j, elementul (i, j) va fi 0.
 *
 * De exemplu, într-o matrice tranzitivă pentru un graf orientat, dacă există o călătorie de la nodul 1 la nodul 2 și o
 * călătorie de la nodul 2 la nodul 3, atunci există o călătorie directă sau indirectă de la nodul 1 la nodul 3, iar
 * elementul (1, 3) va fi 1.
 */

#include "ex2.h"
#include <iostream>
#include <vector>

using namespace std;

// Funcție pentru a calcula închiderea tranzitivă a unui graf orientat
vector<vector<int>> Inchidere_tranzativa(const vector<vector<int>>& graf) {
    int nr_varfuri = graf.size();
    vector<vector<int>> inchidere(nr_varfuri, vector<int>(nr_varfuri, 0));

    // Inițializăm închiderea tranzitivă cu graful original
    for (int i = 0; i < nr_varfuri; ++i) {
        for (int j = 0; j < nr_varfuri; ++j) {
            inchidere[i][j] = graf[i][j];
        }
    }

    // Aplicăm algoritmul lui Warshall pentru a actualiza închiderea tranzitivă
    for (int k = 0; k < nr_varfuri; ++k) {
        for (int i = 0; i < nr_varfuri; ++i) {
            for (int j = 0; j < nr_varfuri; ++j) {
                inchidere[i][j] = inchidere[i][j] || (inchidere[i][k] && inchidere[k][j]);
            }
        }
    }

    return inchidere;
}

// Funcție pentru a afișa o matrice
void printMatrice(const vector<vector<int>>& matrice) {
    int nr_varfuri = matrice.size();
    for (int i = 0; i < nr_varfuri; ++i) {
        for (int j = 0; j < nr_varfuri; ++j) {
            cout << matrice[i][j] << " ";
        }
        cout << endl;
    }
}

void run_2() {
    // Exemplu de graf orientat reprezentat prin matricea de adiacență
    vector<vector<int>> graf = {
            {1,0,1,0,0,1},
            {1,1,0,0,0,0},
            {0,1,1,0,0,0},
            {0,0,1,1,1,0},
            {0,0,0,0,1,1},
            {0,0,0,0,1,1}

    };

    // Calculăm închiderea tranzitivă a grafului
    vector<vector<int>> inchidere = Inchidere_tranzativa(graf);

    // Afișăm închiderea tranzitivă rezultată
    cout << "Inchiderea tranzitiva a grafului este:\n";
    printMatrice(inchidere);

}

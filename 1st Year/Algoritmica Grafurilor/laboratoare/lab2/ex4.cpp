#include "ex4.h"

/*
 * 4. Pentru un graf dat să se afișeze pe ecran vârfurile descoperite de
 * algoritmul BFS și distanța față de vârful sursă (arborele descoperit).
 */
#include <iostream>
#include <fstream>

using namespace std;
ifstream inn("W:\\Facultate S2\\Algoritmica grafurilor\\teme\\lab2\\graf4.txt");

int viz[102], matr[102][102], c[1002];

void bfs(int nod, int n) {
    int d = 1, first = 0, last = 0, i;
    c[last++] = nod;
    viz[nod] = 1;
    while (first != last) {
        for (i = 1; i <= n; i++) {
            if (matr[c[first]][i] and !viz[i]) {
                cout << "Nodul " << i << ", " << " Distanta fata de nodul de start " << nod << " = " << d << '\n';
                c[last] = i;
                last++;
                viz[i] = 1;
            }
        }
        first++;
        d++;
    }
}

void run_4() {
    int x, y, n, nod;
    inn >> n;
    while (inn >> x >> y)
        matr[x][y] = 1;
    inn.close();
    cin >> nod;
    bfs(nod, n);
}

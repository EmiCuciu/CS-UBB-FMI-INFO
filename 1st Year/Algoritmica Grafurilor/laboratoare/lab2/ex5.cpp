#include "ex5.h"
/*
 * 5. Pentru un graf dat să se afișeze pe ecran vârfurile descoperite
 * de apelul recursiv al procedurii DFS_VISIT(G, u) (apadurea descoperită de DFS).
 */
#include <iostream>
#include <fstream>
using namespace std;
ifstream intrare("W:\\Facultate S2\\Algoritmica grafurilor\\teme\\lab2\\graf.txt");

int vizit[102],matrice[102][102];

void dfs(int nod, int n)
{
    int i;
    for (i = 1; i <= n; i++)
    {
        if(!vizit[i] and matrice[nod][i])
        {
            cout << i << " ";
            vizit[i] = 1;
            dfs(i,n);
        }
    }
}

void run_5()
{
    int x,y,n,nod;
    intrare >> n;
    while(intrare >> x >> y)
        matrice[x][y] = 1;
    intrare.close();
    cin >> nod;
    dfs(nod, n);
}

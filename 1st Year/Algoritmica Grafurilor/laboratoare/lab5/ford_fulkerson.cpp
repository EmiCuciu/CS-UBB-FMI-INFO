//
// Created by Emi on 5/17/2024.
//

#include "ford_fulkerson.h"


/**
 * Functia BFS realizeaza o parcurgere in latime a grafului pentru a gasi o cale de la sursa la destinatie prin muchiile
 * cu capacitatea mai mare decat 0.
 *
 * @param graf - graful in care se cauta calea
 * @param sursa - nodul sursa
 * @param destinatie - nodul destinatie
 * @param parinte - vectorul de parinti folosit pentru a reconstitui calea
 * @return true daca exista o cale de la sursa la destinatie, false altfel
 */
bool BFS(std::vector<std::vector<int>>& graf, int sursa, int destinatie, std::vector<int>& parinte) {
    std::queue<int> coada;

    int dimensiune = (int) parinte.size();
    std::vector<bool> vizitat(dimensiune, false);

    vizitat[sursa] = true;
    parinte[sursa] = -1;

    coada.push(sursa);
    while (!coada.empty()) {
        int curent = coada.front();
        coada.pop();

        for (int i = 0; i < dimensiune; i++) {
            if (!vizitat[i] && graf[curent][i] > 0) {
                if (i == destinatie) {
                    parinte[i] = curent;
                    return true;
                }

                coada.push(i);
                parinte[i] = curent;
                vizitat[i] = true;
            }
        }
    }

    return false;
}

/**
 * Functia FORD_FULKERSON implementeaza algoritmul Ford-Fulkerson pentru a gasi fluxul maxim intr-un graf.
 * Algoritmul functioneaza prin gasirea de cai de la sursa la destinatie cu ajutorul functiei BFS si prin actualizarea
 * fluxului pe aceste cai.
 *
 * @param graf - graful in care se cauta fluxul maxim
 * @param sursa - nodul sursa
 * @param destinatie - nodul destinatie
 * @return fluxul maxim de la sursa la destinatie
 */
int FORD_FULKERSON(std::vector<std::vector<int>>& graf, int sursa, int destinatie) {
    int flux_maxim = 0;
    int dimensiune = (int) graf.size();
    std::vector<int> parinte(dimensiune);

    while (BFS(graf, sursa, destinatie, parinte)) {
        int flux_cale = INT_MAX;

        for (int i = destinatie; i != sursa; i = parinte[i]) {
            int temp = parinte[i];
            flux_cale = std::min(flux_cale, graf[temp][i]);
        }

        for (int i = destinatie; i != sursa; i = parinte[i]) {
            int temp = parinte[i];
            graf[temp][i] -= flux_cale;
            graf[i][temp] += flux_cale;
        }

        flux_maxim += flux_cale;
    }

    return flux_maxim;
}

void ff() {
    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\ff_in.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\ff_out.txt)");

    int noduri, muchii;
    in >> noduri >> muchii;

    std::vector<std::vector<int>> graf(noduri, std::vector<int>(noduri));

    int x, y, capacitate;
    for (int i = 0; i < muchii; i++) {
        in >> x >> y >> capacitate;
        graf[x][y] = capacitate;
    }

    int flux_maxim = FORD_FULKERSON(graf, 0, noduri - 1);
    out << flux_maxim;

}
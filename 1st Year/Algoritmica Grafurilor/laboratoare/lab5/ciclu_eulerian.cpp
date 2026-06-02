//
// Created by Emi on 5/17/2024.
//

#include "ciclu_eulerian.h"

std::vector<int> ciclu = {};
int puncte = 0;

/**
 * Functia CICLU_EULERIAN realizeaza un ciclu eulerian, adica un ciclu care trece prin fiecare muchie a grafului o singura data.
 * Functia parcurge recursiv graful, eliminand muchiile pe care le viziteaza si adaugand nodurile in ciclu.
 *
 * @param graf - graful in care se cauta ciclul eulerian
 * @param punct - nodul curent din parcurgerea grafului
 */

void CICLU_EULERIAN(std::vector<std::vector<int>>& graf, int punct) {
    for (int i = 0; i < (int) graf.size(); i++) {
        if (graf[punct][i]) {
            graf[punct][i] = graf[i][punct] = 0;
            CICLU_EULERIAN(graf, i);
        }
    }

    ciclu[++puncte] = punct;
}

void ce() {

    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\ce_in.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\ce_out.txt)");

    int noduri, muchii;
    in >> noduri >> muchii;

    std::vector<std::vector<int>> graf(noduri, std::vector<int>(noduri));
    ciclu = std::vector<int>(noduri);

    int x, y;
    for (int i = 0; i < muchii; i++) {
        in >> x >> y;
        graf[x][y] = 1;
        graf[y][x] = 1;
    }

    CICLU_EULERIAN(graf, 0);
    for (int i = 1; i < puncte; i++) {
        out << ciclu[i] << ' ';
    }

}
//
// Created by Emi on 5/17/2024.
//

#ifndef LAB4_P5_H
#define LAB4_P5_H

#include <algorithm>
#include <fstream>
#include <iostream>
#include <vector>

class UniuneCautare {
private:
    std::vector<int> parinte;
    std::vector<int> rang;

public:
    explicit UniuneCautare(int dimensiune) : parinte(dimensiune), rang(dimensiune, 0) {
        for (int i = 0; i < dimensiune; ++i) {
            parinte[i] = i;
        }
    };
    int gasesteNod(int nod) {
        int radacina = nod;

        while (radacina != parinte[radacina]) {
            radacina = parinte[radacina];
        }

        while (nod != radacina) {
            int urmator = parinte[nod];
            parinte[nod] = radacina;
            nod = urmator;
        }

        return radacina;
    }
    void unesteNoduri(int nod1, int nod2) {
        int rad1 = gasesteNod(nod1);
        int rad2 = gasesteNod(nod2);

        if (rad1 != rad2) {
            if (rang[rad1] > rang[rad2]) {
                parinte[rad2] = rad1;
            } else {
                parinte[rad2] = rad1;
                if (rang[rad2] == rang[rad1]) {
                    rang[rad2]++;
                }
            }
        }
    }
};

auto sorteazaArbore(std::vector<std::pair<std::pair<int, int>, int>>& arbore);

auto Kruskal(std::vector<std::pair<std::pair<int, int>, int>>& arbore, int src);

void p5();

#endif //LAB4_P5_H

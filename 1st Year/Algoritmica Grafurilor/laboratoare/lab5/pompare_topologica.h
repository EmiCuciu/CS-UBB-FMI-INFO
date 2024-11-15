//
// Created by Emi on 5/17/2024.
//

#ifndef LAB5_POMPARE_TOPOLOGICA_H
#define LAB5_POMPARE_TOPOLOGICA_H

#include <iostream>
#include <fstream>
#include <vector>
#include <climits>

using namespace std;

struct Muchie {
    int flux, capacitate;
    int x, y;
};

struct Nod {
    int h, flux_exces;
};

class Graf {
private:
    int V;

    vector<Nod> noduri;
    vector<Muchie> muchii;

    bool impinge(int u);
    void reeticheteaza(int u);
    void preflux(int s);
    void actualizeazaFluxMuchieInversa(int i, int flux);

public:
    Graf(int V);
    void adaugaMuchie(int u, int v, int w);
    int obtineFluxMaxim(int s, int t);
};

void pt();



#endif //LAB5_POMPARE_TOPOLOGICA_H

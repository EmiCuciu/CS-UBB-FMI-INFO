//
// Created by Emi on 5/17/2024.
//

#include "pompare_topologica.h"

/**
 * Constructorul clasei Graf initializeaza numarul de noduri si creeaza o lista de noduri.
 *
 * @param V - numarul de noduri din graf
 */
Graf::Graf(int V) {
    this->V = V;
    for (int i = 0; i < V; i++)
        noduri.push_back({ 0, 0 });
}

/**
 * Functia adaugaMuchie adauga o muchie la lista de muchii a grafului.
 *
 * @param u - nodul de start al muchiei
 * @param v - nodul de sfarsit al muchiei
 * @param capacitate - capacitatea muchiei
 */
void Graf::adaugaMuchie(int u, int v, int capacitate) {
    muchii.push_back({ 0, capacitate, u, v });
}

/**
 * Functia preflux initializeaza prefluxul pentru algoritmul de pompare topologica.
 * Aceasta seteaza inaltimea nodului sursa la numarul total de noduri si initializeaza fluxul pe fiecare muchie care pleaca din sursa.
 *
 * @param s - nodul sursa
 */
void Graf::preflux(int s) {
    noduri[s].h = (int) noduri.size();

    for (int i = 0; i < muchii.size(); i++)
    {
        if (muchii[i].x == s)
        {
            muchii[i].flux = muchii[i].capacitate;
            noduri[muchii[i].y].flux_exces += muchii[i].flux;
            muchii.push_back({-muchii[i].flux, 0, muchii[i].y, s });
        }
    }
}

/**
 * Functia nodCuExcesDeFlux gaseste un nod cu flux in exces.
 * Aceasta parcurge lista de noduri si returneaza primul nod care are un flux in exces.
 *
 * @param noduri - lista de noduri
 * @return indexul nodului cu flux in exces sau -1 daca nu exista un astfel de nod
 */
int nodCuExcesDeFlux(vector<Nod>& noduri)
{
    for (int i = 1; i < noduri.size() - 1; i++)
        if (noduri[i].flux_exces > 0)
            return i;
    return -1;
}

/**
 * Functia actualizeazaFluxMuchieInversa actualizeaza fluxul pe muchia inversa.
 * Aceasta cauta muchia inversa in lista de muchii si scade fluxul de pe aceasta.
 *
 * @param i - indexul muchiei
 * @param flux - fluxul care trebuie scazut
 */
void Graf::actualizeazaFluxMuchieInversa(int i, int flux)
{
    int u = muchii[i].y, v = muchii[i].x;

    for (int j = 0; j < muchii.size(); j++) {
        if (muchii[j].y == v && muchii[j].x == u) {
            muchii[j].flux -= flux;
            return;
        }
    }

    Muchie e = { 0, flux, u, v };
    muchii.push_back(e);
}

/**
 * Functia impinge incearca sa impinga fluxul de pe un nod pe muchiile sale.
 * Aceasta parcurge lista de muchii si, daca gaseste o muchie cu capacitatea mai mare decat fluxul, impinge fluxul pe aceasta muchie.
 *
 * @param u - nodul de pe care se incearca impingerea fluxului
 * @return true daca s-a putut impinge fluxul, false altfel
 */
bool Graf::impinge(int u) {
    for (int i = 0; i < muchii.size(); i++) {
        if (muchii[i].x == u) {
            if (muchii[i].flux == muchii[i].capacitate) {
                continue;
            }

            if (noduri[u].h > noduri[muchii[i].y].h) {
                int flux = min(muchii[i].capacitate - muchii[i].flux,noduri[u].flux_exces);
                noduri[u].flux_exces -= flux;
                noduri[muchii[i].y].flux_exces += flux;
                muchii[i].flux += flux;
                actualizeazaFluxMuchieInversa(i, flux);
                return true;
            }
        }
    }

    return false;
}

/**
 * Functia reeticheteaza reeticheteaza un nod.
 * Aceasta parcurge lista de muchii si, daca gaseste o muchie cu capacitatea mai mare decat fluxul, creste inaltimea nodului.
 *
 * @param u - nodul care trebuie reetichetat
 */
void Graf::reeticheteaza(int u) {
    int mh = INT_MAX;

    for (int i = 0; i < muchii.size(); i++) {
        if (muchii[i].x == u) {
            if (muchii[i].flux == muchii[i].capacitate) {
                continue;
            }

            if (noduri[muchii[i].y].h < mh) {
                mh = noduri[muchii[i].y].h;
                noduri[u].h = mh + 1;
            }
        }
    }
}

/**
 * Functia obtineFluxMaxim calculeaza fluxul maxim intr-un graf folosind algoritmul de pompare topologica.
 * Aceasta initializeaza prefluxul, apoi incearca sa impinga fluxul de pe nodurile cu flux in exces si sa reeticheteze nodurile de pe care nu se poate impinge fluxul.
 *
 * @param s - nodul sursa
 * @param t - nodul destinatie
 * @return fluxul maxim de la sursa la destinatie
 */
int Graf::obtineFluxMaxim(int s, int t) {
    preflux(s);

    while (nodCuExcesDeFlux(noduri) != -1) {
        int u = nodCuExcesDeFlux(noduri);
        if (!impinge(u)) {
            reeticheteaza(u);
        }
    }

    return noduri.back().flux_exces;
}

void pt() {
    std::ifstream in(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\pt_in.txt)");
    std::ofstream out(R"(W:\Facultate S2\Algoritmica grafurilor\teme\lab5\pt_out.txt)");

    int noduri, muchii;
    in >> noduri >> muchii;

    Graf graf(noduri);

    int x, y, capacitate;
    for (int i = 0; i < muchii; i++)
    {
        in >> x >> y >> capacitate;
        graf.adaugaMuchie(x, y, capacitate);
    }

    out << graf.obtineFluxMaxim(0, noduri - 1);

}
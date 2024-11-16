#include "Colectie.h"
#include "IteratorColectie.h"
#include <iostream>

using namespace std;

bool rel(TElem e1, TElem e2) {
    return e1 <= e2;
}

//Best Case = Worst Case = Average Case = Θ(m)
//Worst Case = Θ(n)
//Average Case = Θ(n)
//Overall = O(n)
Colectie::Colectie() {
    capacitate = 10;
    dimensiune = 0;

    elem = new TElem[capacitate];
    prec = new int[capacitate];
    urm = new int[capacitate];

    for (int i = 0; i < capacitate - 1; ++i)
        urm[i] = i + 1;
    urm[capacitate - 1] = -1;

    primLiber = 0;
    prim = ultim = -1;
}

//Best Case = Worst Case = Average Case = Θ(m)
void Colectie::redim() {
    TElem *elemNou = new TElem[capacitate * 2];
    int *precNou = new int[capacitate * 2];
    int *urmNou = new int[capacitate * 2];

    for (int i = 0; i < capacitate; ++i) {
        elemNou[i] = elem[i];
        precNou[i] = prec[i];
        urmNou[i] = urm[i];
    }

    delete[]elem;
    delete[]prec;
    delete[]urm;

    elem = elemNou;
    prec = precNou;
    urm = urmNou;

    capacitate *= 2;

    for (int i = capacitate / 2; i < capacitate - 1; ++i)
        urm[i] = i + 1;
    urm[capacitate - 1] = -1;
    primLiber = capacitate / 2;
}

//Best Case = Worst Case = Average Case = Θ(1)
int Colectie::aloca() {
    int poz = primLiber;
    primLiber = urm[primLiber];
    dimensiune++;
    return poz;
}

//Best Case = Worst Case = Average Case = Θ(1)
void Colectie::dealoca(int poz) {
    urm[poz] = primLiber;
    primLiber = poz;
    dimensiune--;
}

//Best Case = Θ(1)
//Worst Case = Θ(n)
//Average Case = Θ(n)
//Overall = O(n)
void Colectie::adauga(TElem e) {
    if (primLiber == -1)
        redim();

    int pozElement = aloca();
    elem[pozElement] = e;

    if (prim == -1 && ultim == -1) {
        urm[pozElement] = prec[pozElement] = -1;
        prim = ultim = pozElement;
    } else if (rel(e, elem[prim])) {
        prec[pozElement] = -1;
        urm[pozElement] = prim;
        prec[prim] = pozElement;
        prim = pozElement;
    } else if (rel(elem[ultim], e)) {
        prec[pozElement] = ultim;
        urm[pozElement] = -1;
        urm[ultim] = pozElement;
        ultim = pozElement;
    } else {
        int poz = prim;
        while (!rel(e, elem[poz]))
            poz = urm[poz];
        prec[pozElement] = prec[poz];
        urm[pozElement] = poz;
        urm[prec[poz]] = pozElement;
        prec[poz] = pozElement;
    }
}

//Best Case = Θ(1)
//Worst Case = Θ(n)
//Average Case = Θ(n)
//Overall = O(n)
bool Colectie::sterge(TElem e) {
    if (prim == -1 && ultim == -1)
        return false;

    if (prim == ultim && elem[prim] == e) {
        dealoca(prim);
        prim = ultim = -1;
        return true;
    }

    if (elem[prim] == e) {
        int primNou = urm[prim];
        dealoca(prim);
        prim = primNou;
        return true;
    }

    int poz = prim;
    while (poz != -1 && elem[poz] != e && rel(elem[poz], e))
        poz = urm[poz];

    if (poz != -1 && elem[poz] == e) {
        urm[prec[poz]] = urm[poz];
        if (poz == ultim)
            ultim = prec[poz];
        else
            prec[urm[poz]] = prec[poz];
        dealoca(poz);
        return true;
    }

    return false;
}

//Best Case = Θ(1)
//Worst Case = Θ(n)
//Average Case = Θ(n)
//Overall = O(n)
bool Colectie::cauta(TElem e) const {
    int poz = prim;
    while (poz != -1 && elem[poz] != e && rel(elem[poz], e))
        poz = urm[poz];
    return poz != -1 && elem[poz] == e;
}

//Best Case = Θ(1)
//Worst Case = Θ(n)
//Average Case = Θ(n)
//Overall = O(n)
int Colectie::nrAparitii(TElem e) const {
    int poz = prim;
    while (poz != -1 && elem[poz] != e && rel(elem[poz], e))
        poz = urm[poz];

    if (poz == -1 || elem[poz] != e)
        return 0;
    int ct = 0;
    while (poz != -1 && elem[poz] == e) {
        poz = urm[poz];
        ct++;
    }
    return ct;
}

//Best Case = Worst Case = Average Case = Θ(1)
int Colectie::dim() const {
    return dimensiune;
}

//Best Case = Worst Case = Average Case = Θ(1)
bool Colectie::vida() const {
    return dimensiune == 0;
}

//Best Case = Worst Case = Average Case = Θ(1)
IteratorColectie Colectie::iterator() const {
    return IteratorColectie(*this);
}

//Best Case = Worst Case = Average Case = Θ(1)
Colectie::~Colectie() {
    delete[]elem;
    delete[]urm;
    delete[]prec;
    capacitate = dimensiune = 0;
}

//Best Case = Θ(n)
//Worst Case = Θ(n^2)
//Average Case = Θ(n^2)
//Overall = O(n^2)
int Colectie::transformaInMultime() {
    if (dim() < 2)
        return 0;
    int nrSterse = 0;
    int poz = prim;
    while (urm[poz] != -1) {
        int urmPoz = urm[poz];
        if (elem[poz] == elem[urmPoz]) {
            sterge(elem[poz]);
            nrSterse++;
        }
        poz = urmPoz;
    }
    return nrSterse;
}

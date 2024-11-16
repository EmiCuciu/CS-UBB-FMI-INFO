#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>

using namespace std;
/**
 * relatia de ordine intre elemente
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * @param e1 primul element
 * @param e2 al doilea element
 * @return true daca e1 <= e2, false altfel
 */
bool rel(TElem e1, TElem e2) {
    return e1 <= e2;
}

/**
 * constructorul colectiei
 * complexitate timp: θ(n), unde n este capacitatea inițială a tabloului
 * complexitate spatiu: θ(n)
 */
Colectie::Colectie() {
    cap = 10;
    elems = new Nod[cap];
    prim = -1;
    ultim = -1;
    firstFree = 0;
    for (int i = 0; i < cap - 1; i++) {
        elems[i].urm = i + 1;
    }
    elems[cap - 1].urm = -1;
    nrElemente = 0;
}

/**
 * redimensioneaza tabloul dinamic
 * complexitate timp: θ(n), unde n este capacitatea actuală a tabloului
 * complexitate spatiu: θ(n)
 */
void Colectie::redimensioneaza() {
    int newCap = 2 * cap;
    Nod* newElems = new Nod[newCap];
    for (int i = 0; i < cap; i++) {
        newElems[i] = elems[i];
    }
    for (int i = cap; i < newCap - 1; i++) {
        newElems[i].urm = i + 1;
    }
    newElems[newCap - 1].urm = -1;
    firstFree = cap;
    cap = newCap;
    delete[] elems;
    elems = newElems;
}

/**
 * adauga un element in colectie
 * complexitate timp: caz favorabil = caz defavorabil = caz mediu = θ(1) amortizat (din cauza redimensionarii)
 * complexitate spatiu: θ(1)
 * @param e elementul ce se va adauga in colectie
 */
void Colectie::adauga(TElem e) {
    if (firstFree == -1) {
        redimensioneaza();
    }

    int nou = firstFree;
    firstFree = elems[firstFree].urm;

    elems[nou].e = e;
    elems[nou].urm = -1;
    elems[nou].prec = -1;

    if (prim == -1) {
        prim = nou;
        ultim = nou;
    }
    else {
        int curent = prim;
        int prec = -1;
        while (curent != -1 && rel(elems[curent].e, e)) {
            prec = curent;
            curent = elems[curent].urm;
        }
        if (curent == prim) {
            elems[nou].urm = prim;
            elems[prim].prec = nou;
            prim = nou;
        }
        else if (curent == -1) {
            elems[ultim].urm = nou;
            elems[nou].prec = ultim;
            ultim = nou;
        }
        else {
            elems[nou].urm = curent;
            elems[nou].prec = prec;
            elems[prec].urm = nou;
            elems[curent].prec = nou;
        }
    }
    nrElemente++;
}

/**
 * sterge un element din colectie
 * complexitate timp: caz favorabil = θ(1), caz defavorabil = caz mediu = θ(n)
 * complexitate spatiu: θ(1)
 * @param e elementul ce se va sterge din colectie
 * @return true daca elementul a fost sters, false altfel
 */
bool Colectie::sterge(TElem e) {
    int curent = prim;
    while (curent != -1 && elems[curent].e != e) {
        curent = elems[curent].urm;
    }
    if (curent == -1) {
        return false;
    }
    else {
        if (curent == prim) {
            prim = elems[prim].urm;
            if (prim != -1) {
                elems[prim].prec = -1;
            }
            else {
                ultim = -1;
            }
        }
        else if (curent == ultim) {
            ultim = elems[ultim].prec;
            if (ultim != -1) {
                elems[ultim].urm = -1;
            }
        }
        else {
            int prec = elems[curent].prec;
            int urm = elems[curent].urm;
            elems[prec].urm = urm;
            elems[urm].prec = prec;
        }
        elems[curent].urm = firstFree;
        firstFree = curent;
        nrElemente--;
        return true;
    }
}

/**
 * verifica daca un element se afla in colectie
 * complexitate timp: caz favorabil = θ(1), caz defavorabil = caz mediu = θ(n)
 * complexitate spatiu: θ(1)
 * @param e elementul ce se va cauta in colectie
 * @return true daca elementul a fost gasit, false altfel
 */
bool Colectie::cauta(TElem e) const {
    int curent = prim;
    while (curent != -1) {
        if (elems[curent].e == e) {
            return true;
        }
        curent = elems[curent].urm;
    }
    return false;
}

/**
 * returneaza numarul de aparitii ale unui element in colectie
 * complexitate timp: caz favorabil = θ(1), caz defavorabil = caz mediu = θ(n)
 * complexitate spatiu: θ(1)
 * @param e elementul ce se va cauta in colectie
 * @return numarul de aparitii ale elementului in colectie
 */
int Colectie::nrAparitii(TElem e) const {
    int count = 0;
    int curent = prim;
    while (curent != -1) {
        if (elems[curent].e == e) {
            count++;
        }
        curent = elems[curent].urm;
    }
    return count;
}

/**
 * returneaza numarul de elemente din colectie
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * @return numarul de elemente din colectie
 */
int Colectie::dim() const {
    return nrElemente;
}

/**
 * verifica daca colectia este goala
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * @return true daca colectia este goala, false altfel
 */
bool Colectie::vida() const {
    return prim == -1;
}

/**
 * creeaza un iterator pentru colectie
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * @return iteratorul colectiei
 */
IteratorColectie Colectie::iterator() const {
    return IteratorColectie(*this);
}

/**
 * destructorul colectiei
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(n)
 */
Colectie::~Colectie() {
    delete[] elems;
}

/**
 * transforma colectia in multime
 * Sterge toate elementele duplicate din colectie si returneaza numarul de elemente sterse din colectie
 * complexitate timp: caz favorabil = caz defavorabil = caz mediu = O(n^2)
 * complexitate spatiu: θ(1)
 * @return nr de elemente sterse din colectie
 */
int Colectie::transformaInMultime() {
    int nr_total_sterse = 0;
    int curent = ultim;  // Pornim de la ultimul element

    while (curent != -1) {
        int anterior = elems[curent].prec;
        while (anterior != -1) {
            if (elems[anterior].e == elems[curent].e) {
                int antt = elems[anterior].prec;
                if (sterge(elems[anterior].e)) {  
                    nr_total_sterse++;
                }
                anterior = antt; 
            }
            else {
                anterior = elems[anterior].prec;
            }
        }
        curent = elems[curent].prec;
    }

    return nr_total_sterse;
}




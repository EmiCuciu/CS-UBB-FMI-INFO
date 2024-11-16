#include "IteratorColectie.h"
#include <exception>

using namespace std;

/**
 * constructorul iteratorului
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 */
IteratorColectie::IteratorColectie(const Colectie& c) : col(c) {
    curent = col.prim;
}

/**
 * reseteaza pozitia iteratorului la inceputul colectiei
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 */
void IteratorColectie::prim() {
    curent = col.prim;
}

/**
 * muta iteratorul pe urmatoarea pozitie din colectie
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 */
void IteratorColectie::urmator() {
    if (curent != -1) {
        curent = col.elems[curent].urm;
    }
}

/**
 * verifica daca iteratorul este valid (poziția curentă este validă)
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * @return true daca iteratorul este valid, false altfel
 */
bool IteratorColectie::valid() const {
    return curent != -1;
}

/**
 * returneaza elementul curent al iteratorului
 * complexitate timp: θ(1)
 * complexitate spatiu: θ(1)
 * arunca exceptie daca iteratorul nu este valid
 */
TElem IteratorColectie::element() const {
    if (curent != -1) {
        return col.elems[curent].e;
    }
    else {
        throw exception();
    }
}

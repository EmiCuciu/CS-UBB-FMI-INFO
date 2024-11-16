#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>

//Best Case = Worst Case = Average Case = Θ(1)
IteratorColectie::IteratorColectie(const Colectie &c) : col(c) {
    poz = col.prim;
}

//Best Case = Worst Case = Average Case = Θ(1)
TElem IteratorColectie::element() const {
    if (!valid())
        throw std::exception();
    return col.elem[poz];
}

//Best Case = Worst Case = Average Case = Θ(1)
bool IteratorColectie::valid() const {
    return poz != -1;
}

//Best Case = Worst Case = Average Case = Θ(1)
void IteratorColectie::urmator() {
    if (!valid())
        throw std::exception();
    poz = col.urm[poz];
}

//Best Case = Worst Case = Average Case = Θ(1)
void IteratorColectie::prim() {
    poz = col.prim;
}

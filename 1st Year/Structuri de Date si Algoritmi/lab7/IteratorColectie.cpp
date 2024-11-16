#include "IteratorColectie.h"
#include "Colectie.h"


IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
        curent = c.prim;
}

TElem IteratorColectie::element() const{
    if(!valid())
        throw std::exception();
	return col.el[curent];
}

bool IteratorColectie::valid() const {
	return curent != -1;
}

void IteratorColectie::urmator() {
    if(valid()) {
            curent = col.urm[curent];
    }
    else
        throw std::exception();
}

void IteratorColectie::prim() {
    curent = col.prim;
}

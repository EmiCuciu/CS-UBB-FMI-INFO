#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>

IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    pozCurent = 0;
    frecvCurenta = 0;
    deplasare();
}

void IteratorColectie::prim() {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    pozCurent = 0;
    deplasare();
}

void IteratorColectie::urmator() {
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)
    if (!valid()) {
        throw std::exception();
    }
    if (frecvCurenta > 1) {
        frecvCurenta--;
    } else {
        curent = curent->urm;
        frecvCurenta = 0;
        while (curent == nullptr && pozCurent < col.m - 1) {
            pozCurent++;
            curent = col.l[pozCurent];
        }
        if (curent != nullptr) {
            frecvCurenta = curent->frecv;
        }
    }
}


bool IteratorColectie::valid() const {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    return pozCurent < col.m and curent != nullptr;
}

TElem IteratorColectie::element() const {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    if (valid()){
        return curent->e;
    } else {
        throw std::exception();
    }
}

void IteratorColectie::deplasare(){
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)
    while (pozCurent<col.m && col.l[pozCurent]==nullptr) pozCurent++;
    if (pozCurent<col.m)
        curent=col.l[pozCurent];
    if (curent != nullptr) {
        frecvCurenta = curent->frecv;
    }
}

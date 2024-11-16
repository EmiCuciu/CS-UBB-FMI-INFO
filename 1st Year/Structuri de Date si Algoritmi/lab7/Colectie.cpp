#include "Colectie.h"
#include "IteratorColectie.h"
#include <iostream>

using namespace std;

bool rel(TElem e1, TElem e2) {
	return e1 >= e2;
}

//tetha(cap)
Colectie::Colectie() {
    cap = 10;
	relatie = rel;
    len = 0;
    urm = new int[cap];
    prec = new int[cap];
    el = new TElem[cap];
    //frecventa=new TElem [cap];
    initSpatiuLiber(0);
    prim = -1;
}

//tetha(1)
int Colectie::aloca() {
    //aloca un spatiu liber de indice i
    //sterge primul nod din lista spatiului liber
    int i = primLiber;
    primLiber = urm[primLiber];
    return i;
}

//tetha(1)
void Colectie::dealoca(int i) {
    urm[i] = primLiber;
    primLiber = i;
}

//tetha(cap)
void Colectie::initSpatiuLiber(int a) {
    for(int i=a; i<cap-1; i++)
        urm[i] = i+1;
    urm[cap-1] = -1;
    //frecventa[cap-1] = 0;
    primLiber = a;
}

// Best case: tetha(1)
// Worst case: tetha(cap)
// Average case: O(cap)
int Colectie::creeazaNod(TElem e){
    if(primLiber == -1){
        int cap_veche = cap;
        cap *= 2;
        int* newurm = new int[cap];
        int* newprec = new int[cap];
        TElem* newel = new TElem[cap];
        //int* newfrecventa = new int[cap];

        for (int k = 0; k < cap_veche; k++) {
            newurm[k] = urm[k];
            newprec[k] = prec[k];
            newel[k] = el[k];
            //newfrecventa[k] = frecventa[k];
        }

        delete[] urm;
        delete[] prec;
        delete[] el;
        //delete[] frecventa;

        //frecventa=newfrecventa;
        urm = newurm;
        prec = newprec;
        el = newel;

        initSpatiuLiber(cap_veche);
    }
    int i = aloca();
    el[i] = e;
    urm[i] = -1;
    prec[i] = -1;
    //frecventa[i]=1;

    return i;
}

int Colectie::cauta_ind(TElem e) {
    int i = prim;
    while(i != -1){
        if(e == el[i])
            return i;
        i = urm[i];
    }
    return -1;
}

//tetha(n)
void Colectie::adauga(TElem e) {
    //cream un nod nou

        int i = creeazaNod(e);
        if (len == 0) {
            prim = i;
        } else {
            int p = prim;
            while (p != -1) {
                if (relatie(e, el[p]) && p == prim) {
                    urm[i] = p;
                    prec[p] = i;
                    prim = i;
                    break;

                } else if (relatie(e, el[p])) {
                    urm[i] = p;
                    urm[prec[p]] = i;
                    prec[i] = prec[p];
                    prec[p] = i;
                    break;
                }
                int aux = p;
                p = urm[p];
                if (p == -1) {
                    urm[aux] = i;
                    prec[i] = aux;
                }
            }

    }
    len++;
}

// Best case: tetha(1)
// Worst case: tetha(n)
// Average case: O(n)
bool Colectie::sterge(TElem e) {
    int i = prim;
    while(i != -1){
        if(el[i] == e) {
            if (i == prim) {
                prim = urm[i];
                if(prim != -1)
                    prec[prim] = -1;
            } else {
                urm[prec[i]] = urm[i];
                if(urm[i] != -1)
                    prec[urm[i]] = prec[i];
            }
            dealoca(i);
            len--;
            return true;
        }
        i = urm[i];
    }
    return false;
}

// Best case: tetha(1)
// Worst case: tetha(n)
// Average case: O(n)
bool Colectie::cauta(TElem elem) const {
    int i = prim;
    while(i != -1){
        if(elem == el[i])
            return true;
        i = urm[i];
    }
    return false;
}

// Best case: tetha(1)
// Worst case: tetha(n)
// Average case: O(n)
int Colectie::nrAparitii(TElem elem) const {
    int nr = 0;
    int i = prim;
    while(i != -1){
        if(el[i] == elem)
            nr++;
        i = urm[i];
    }
	return nr;
}



int Colectie::dim() const {
	return len;
}


bool Colectie::vida() const {
	return len == 0;
}


IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {
    delete[] urm;
    delete[] prec;
    delete[] el;
}

//tetha(n)
void Colectie::adaugaToateElementele(const Colectie& b){
    int i = b.prim;
    while(i != -1){
        adauga(b.el[i]);
        i = urm[i];
    }
}


//Subalgoritm adaugaToateElementele(colectieB)
//  i<-colectieB.prim
//  Cat timp i != -1 executa
//      adauga(colectieB.el[i])
//      i<-urm[i]
//  SfCatTimp
//SfSubalgoritm
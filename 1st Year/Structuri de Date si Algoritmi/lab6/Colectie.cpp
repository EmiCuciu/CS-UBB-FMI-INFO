#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;

int hashCode(TElem e) {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    return abs(e);
}

int Colectie::d(TElem x) const
{
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
    return hashCode(x) % m;
}


Colectie::Colectie() {
    //Caz favorabil = Caz defavorabil = Caz total = 0(m)
    len = 0;
    m = 10;

    for (int i=0;i<m;i++) {
        l[i] = nullptr;
    }

}


void Colectie::adauga(TElem elem) {
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)
    int i = d(elem);

    Node* head = l[i];
    while (head != nullptr){
        if (head->e == elem){
            head->frecv++;
            len++;
            return;
        }
        head = head->urm;
    }

    Node* p = new Node{elem, 1, l[i]};
    l[i] = p;

    len++;
}

bool Colectie::sterge(TElem elem) {
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)

    int index = d(elem);
    Node* entry = l[index];
    Node* prev = nullptr;
    while (entry != nullptr) {
        if (entry->e == elem) {
            if (entry->frecv > 1) {
                entry->frecv--;
            } else {
                if (prev == nullptr) {
                    l[index] = entry->urm;
                } else {
                    prev->urm = entry->urm;
                }
                delete entry;
            }
            len--;
            return true;
        }
        prev = entry;
        entry = entry->urm;
    }
    return false;
}


bool Colectie::cauta(TElem elem) const {
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)
    int i = d(elem);

    Node* p = l[i];
    while (p != nullptr)
    {
        if (p->e == elem)
            return true;
        p = p->urm;
    }

    return false;
}

int Colectie::nrAparitii(TElem elem) const {
    //Caz favorabil = 0(1)
    //Caz defavorabil = 0(n)
    //Caz total = O(n)
    int i = d(elem);
    Node* p = l[i];

    while (p != nullptr)
    {
        if (p->e == elem)
            return p->frecv;
        p = p->urm;
    }

    return 0;
}


int Colectie::dim() const {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
	return len;
}


bool Colectie::vida() const {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
	return dim() == 0;
}

IteratorColectie Colectie::iterator() const {
    //Caz favorabil = Caz defavorabil = Caz total = 0(1)
	return  IteratorColectie(*this);
}

Colectie::~Colectie() {
    //Caz favorabil = Caz defavorabil = Caz total = 0(n*m)
	for (int i=0;i<m;i++){
        while(l[i] != nullptr){
            Node* p = l[i];
            l[i]=l[i]->urm;
            delete p;
        }
    }
}

int Colectie::transformaInMultime() {
    //Caz favorabil = 0(m)
    //Caz defavorabil = 0(m*n)
    //Caz total = O(n*m)
    /*
     * transformaInMultime()
     *
     * count <- 0
     * pentru i <- 0,m executa
     *      nod p <- l[i]
     *      cat timp p != null executa
     *          cat timp p.frecv > 1 executa
     *              sterge(p.e)
     *              count <- count + 1
     *          sfCatTimp
     *          p <- p.urm
     *      sfCatTimp
     * sfPentru
     *
     * sfAlgoritm
     */
    int count=0;
    for (int i=0;i<m;i++){
        Node* p = l[i];
        while (p != nullptr){
            while (p->frecv > 1){
                sterge(p->e);
                count++;
            }
            p = p->urm;
        }
    }
    return count;
}

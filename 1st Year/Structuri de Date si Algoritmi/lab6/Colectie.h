#pragma once
#include <vector>
#include <map>
#define NULL_TELEM -1
typedef int TElem;
using namespace std;

class IteratorColectie;

class Node{
private:
    TElem e;
    int frecv;
    Node* urm;
public:
    friend class Colectie;
    friend class IteratorColectie;
    Node(TElem e, int frecv, Node* urm) : e{e}, frecv{frecv}, urm{urm} {};
};

class Colectie
{
	friend class IteratorColectie;


private:
	int len, m;
    Node* l[10];

    int d(TElem x) const;

public:
		//constructorul implicit
		Colectie();

		//adauga un element in colectie
		void adauga(TElem e);

		//sterge o aparitie a unui element din colectie
		//returneaza adevarat daca s-a putut sterge
		bool sterge(TElem e);

		//verifica daca un element se afla in colectie
		bool cauta(TElem elem) const;

		//returneaza numar de aparitii ale unui element in colectie
		int nrAparitii(TElem elem) const;


		//intoarce numarul de elemente din colectie;
		int dim() const;

		//verifica daca colectia e vida;
		bool vida() const;

		//returneaza un iterator pe colectie
		IteratorColectie iterator() const;

		// destructorul colectiei
		~Colectie();

        //pastreaza doar o aparitie a tuturor elementelor din colectie
        //returneaza numarul de elemente eliminate
        int transformaInMultime();
};


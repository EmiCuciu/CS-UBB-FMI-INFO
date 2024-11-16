#pragma once

typedef int TElem;

//in implementarea operatiilor se va folosi functia (relatia) rel (de ex, pentru <=)
// va fi declarata in .h si implementata in .cpp ca functie externa colectiei
bool rel(TElem e1, TElem e2);


class IteratorColectie;

class Colectie {
    friend class IteratorColectie;

private:
    struct Nod {
        TElem e;
        int urm;
        int prec;
    };

    Nod* elems;       // Tablou dinamic pentru elemente
    int cap;          // Capacitatea tabloului
    int prim;         // Indexul primului element
    int ultim;        // Indexul ultimului element
    int nrElemente;   // Numărul de elemente din colecție
    int firstFree;    // Primul loc liber din tablou

    void redimensioneaza();

public:
    //constructorul implicit
    Colectie();

    //adauga un element in colectie
    void adauga(TElem e);

    //sterge o aparitie a unui element din colectie
    //returneaza adevarat daca s-a putut sterge
    bool sterge(TElem e);

    //verifica daca un element se afla in colectie
    bool cauta(TElem e) const;

    //returneaza numar de aparitii ale unui element in colectie
    int nrAparitii(TElem e) const;

    //intoarce numarul de elemente din colectie;
    int dim() const;

    //verifica daca colectia e vida;
    bool vida() const;

    //returneaza un iterator pe colectie
    IteratorColectie iterator() const;

    //destructorul colectiei
    ~Colectie();

    int transformaInMultime();
};


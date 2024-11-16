#include "ui.h"
#include <stdio.h>
#include <stdlib.h>



void add_ui(service *s) {
    char nume[100], producator[100];
    int cantitate;
    printf("Introduceti numele materiei prime: ");
    scanf("%s", nume);
    printf("Introduceti producatorul materiei prime: ");
    scanf("%s", producator);
    printf("Introduceti cantitatea materiei prime: ");
    scanf("%d", &cantitate);
    if(validare(cantitate, nume, producator) == 1) {
        addMateriePrima(s, nume, producator, cantitate);
        printf("Materie prima adaugata cu succes!\n\n");
    }
    else {
        printf("Datele introduse nu sunt valide!\n");
    }
}

vectMaterieP* delete_ui(service *v){
char nume[100], producator[100];
    printf("Introduceti numele materiei prime: ");
    scanf("%s", nume);
    printf("Introduceti producatorul materiei prime: ");
    scanf("%s", producator);
    return deleteMateriePrima(v, nume, producator);
}

void update_ui(service *v) {
    char nume[100], producator[100], numeNou[100], producatorNou[100];
    int cantitateNoua;
    printf("Introduceti numele materiei prime: ");
    scanf("%s", nume);
    printf("Introduceti producatorul materiei prime: ");
    scanf("%s", producator);
    printf("Introduceti noul nume al materiei prime: ");
    scanf("%s", numeNou);
    printf("Introduceti noul producator al materiei prime: ");
    scanf("%s", producatorNou);
    printf("Introduceti noua cantitate a materiei prime: ");
    scanf("%d", &cantitateNoua);
    if(validare(cantitateNoua, numeNou, producatorNou) == 0) {
        printf("Datele introduse nu sunt valide!\n");
        return;
    }
    updateMateriePrima(v, nume, producator, numeNou, producatorNou, cantitateNoua);
    printf("Materie prima modificata cu succes!\n\n");
}

void print_all_ui(vectMaterieP *v) {
    for (int i = 0; i < size(v); i++) {
        materiePrima *m = getElem(v, i);
        printf("Nume: %s, Producator: %s, Cantitate: %d\n", m->nume, m->producator, m->cantitate);
    }
}

void print_all_service(service *s) {
    for (int i = 0; i < size(s->lista); i++) {
        materiePrima *m = getElem(s->lista, i);
        printf("Nume: %s, Producator: %s, Cantitate: %d\n", m->nume, m->producator, m->cantitate);
    }
}

void filter_and_print_ui(vectMaterieP *v) {
    char litera;
    int cantitateMax;
    printf("1. Filtrare dupa litera\n");
    printf("2. Filtrare dupa cantitate\n");
    int optiune;
    scanf("%d", &optiune);
    if(optiune == 1) {
        printf("Introduceti litera cu care trebuie sa inceapa numele: ");
        scanf(" %c", &litera); // se adauga un spatiu inainte de %c pentru a ignora newline-ul
        for (int i = 0; i < size(v); i++) {
            materiePrima *m = getElem(v, i);
            if (m->nume[0] == litera) {
                printf("Nume: %s, Producator: %s, Cantitate: %d\n", m->nume, m->producator, m->cantitate);
            }
        }
    }
    else if(optiune == 2) {
        printf("Introduceti cantitatea maxima: ");
        scanf("%d", &cantitateMax);
        for (int i = 0; i < size(v); i++) {
            materiePrima *m = getElem(v, i);
            if (m->cantitate < cantitateMax) {
                printf("Nume: %s, Producator: %s, Cantitate: %d\n", m->nume, m->producator, m->cantitate);
            }
        }
    }
    printf("\n");
}

void sort_crescator(vectMaterieP *v) {
    for (int i = 0; i < size(v) - 1; i++) {
        for (int j = i + 1; j < size(v); j++) {
            materiePrima *m1 = getElem(v, i);
            materiePrima *m2 = getElem(v, j);
            if (strcmp(m1->nume, m2->nume) < 0) {
                materiePrima *aux = m1;
                v->vector[i] = m2;
                v->vector[j] = aux;
            }
        }
    }
}

void sort_descrescator(vectMaterieP *v) {
    for (int i = 0; i < size(v) - 1; i++) {
        for (int j = i + 1; j < size(v); j++) {
            materiePrima *m1 = getElem(v, i);
            materiePrima *m2 = getElem(v, j);
            if (strcmp(m1->nume, m2->nume) > 0) {
                materiePrima *aux = m1;
                v->vector[i] = m2;
                v->vector[j] = aux;
            }
        }
    }
}

void sort_ui(vectMaterieP *v) {
    vectMaterieP *v2 = copyVect(v);
    printf("1. Sortare crescator\n");
    printf("2. Sortare descrescator\n");
    int optiune;
    scanf("%d", &optiune);
    if(optiune == 1) {
        sort_crescator(v);
    }
    else if(optiune == 2) {
        sort_descrescator(v);
    }
    printf("Materiile prime sortate:\n");
    print_all_ui(v2);
    eliberareVectMaterieP(v2);
}


///////////////////////// FUNCTII GENERICE ///////////////////////////

typedef int (*ConditionFunction)(materiePrima*);


vectMaterieP* filter(service *s, ConditionFunction condition){
    vectMaterieP *rez = creazaVect();
    for (int i = 0; i < size(s->lista); i++) {
        materiePrima *m = getElem(s->lista, i);
        if (condition(m)) {
            addElem(rez, copieMateriePrima(m));
        }
    }
    return rez;
}
char litera;
int cantitateMax;

int name_starts_with_litera(materiePrima *m) {
    return m->nume[0] == litera;
}

int cantitation(materiePrima *m) {
    return m->cantitate < cantitateMax;
}

void FUNCTII_GENERICE(service *s){
    int optiune;
    printf("1. Filtrare dupa litera\n");
    printf("2. Filtrare dupa cantitate\n");
    printf("Introduceti optiunea: ");
    scanf("%d", &optiune);
    if(optiune == 1) {
        printf("Introduceti litera cu care trebuie sa inceapa numele: ");
        scanf(" %c", &litera);
        vectMaterieP *filtered = filter(s, name_starts_with_litera);
        print_all_ui(filtered);
        eliberareVectMaterieP(filtered);
    }
    else if(optiune == 2) {
        printf("Introduceti cantitatea maxima: ");
        scanf("%d", &cantitateMax);
        vectMaterieP *filtered = filter(s, cantitation);
        print_all_ui(filtered);
        eliberareVectMaterieP(filtered);
    }
}

void run() {
    vectMaterieP *v = creazaVect();
    int optiune;

    service *s = malloc(sizeof(service));
    s->lista = creazaVect();
    s->undo = creazaVect();

    while (1) {
        printf("STORAGE\n");
        printf("-------------------------\n");
        //print_all_ui(v);
        print_all_service(s);
        printf("-------------------------\n\n\n");

        printf("0. Iesire\n");
        printf("1. Adaugare materie prima\n");
        printf("2. Stergere materie prima\n");
        printf("3. Modificare materie prima\n");
        printf("4. Afisare materii prime\n");
        printf("5. Filtrare dupa litera sau cantitate\n");
        printf("6. Sortare materii prime\n");
        printf("7. Undo\n");
        printf("9. FUNCTII GENERICE\n");
        printf("Introduceti optiunea: ");
        scanf("%d", &optiune);
        if (optiune == 1) {
            add_ui(s);
        }
        else if (optiune == 2) {
            v = delete_ui(s);
        }
        else if (optiune == 3) {
            update_ui(s);
        }
        else if (optiune == 4) {
            continue;
        }
        else if (optiune == 5) {
            filter_and_print_ui(v);
        }
        else if (optiune == 6) {
            sort_ui(v);
        }
        else if(optiune == 7){
            undo_action(s);
        }
        else if(optiune == 9){
            FUNCTII_GENERICE(s);
        }


        else if(optiune == 0)
            break;
        else {
            printf("Optiune invalida!\n");
        }
    }
    eliberareVectMaterieP(v);
    eliberareVectMaterieP(s->lista);
    eliberareVectMaterieP(s->undo);
    free(s);
}



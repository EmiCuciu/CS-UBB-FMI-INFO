#ifndef LAB2_DINAMIC_TRANZACTIE_H
#define LAB2_DINAMIC_TRANZACTIE_H

typedef struct {
    int zi;
    float suma;
    char *tip;
    char *descriere;
} Tranzactie;

Tranzactie *creeazaTranzactie(int zi, float suma, char *tip, char *descriere);

void distrugeTranzactie(Tranzactie *tranzactie);

int get_zi(Tranzactie *tranzactie);

float get_suma(Tranzactie *tranzactie);

char *get_tip(Tranzactie *tranzactie);

char *get_descriere(Tranzactie *tranzactie);

void set_zi(Tranzactie *tranzactie, int zi);

void set_suma(Tranzactie *tranzactie, float suma);

void set_tip(Tranzactie *tranzactie, char *tip);

void set_descriere(Tranzactie *tranzactie, char *descriere);



#endif //LAB2_DINAMIC_TRANZACTIE_H

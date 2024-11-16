#include "tranzactie_ui.h"
#include "../Validari/validari.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/*
 * Functie care adauga o tranzactie in lista de tranzactii
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: tranzactia este adaugata in lista de tranzactii
 */
void adauga_tranzactie_ui(SERVICE *service) {
    int zi;
    float suma;
    char *tip = (char *) malloc(20 * sizeof(char));
    char *descriere = (char *) malloc(50 * sizeof(char));
    printf("Ziua: ");
    scanf("%d", &zi);
    printf("Suma: ");
    scanf("%f", &suma);
    printf("Tipul: ");
    scanf("%s", tip);
    printf("Descrierea: ");
    scanf("%s", descriere);
    if (validareTranzactie(zi, suma, tip, descriere) == 1) {
        adaugaTranzactie_service(service, zi, suma, tip, descriere);
        printf("Tranzactie adaugata cu succes!\n");
    } else {
        printf("Tranzactie invalida!\n");
    }
    free(tip);
    free(descriere);
}

/*
 * Functie care sterge o tranzactie din lista de tranzactii
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: tranzactia este stearsa din lista de tranzactii
 */
void sterge_tranzactie_ui(SERVICE *service) {
    int zi;
    char *descriere = (char *) malloc(50 * sizeof(char));
    printf("Ziua: ");
    scanf("%d", &zi);
    printf("Descrierea: ");
    scanf("%s", descriere);
    stergeTranzactie_service(service, zi, descriere);
    printf("Tranzactie stearsa cu succes!\n");
    free(descriere);
}

/*
 * Functie care modifica o tranzactie din lista de tranzactii
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: tranzactia este modificata in lista de tranzactii
 */
void modifica_tranzactie_ui(SERVICE *service) {
    int zi;
    float suma_noua;
    char *tip_nou = (char *) malloc(20 * sizeof(char));
    char *descriere = (char *) malloc(50 * sizeof(char));
    char *descriere_noua = (char *) malloc(50 * sizeof(char));
    printf("Ziua: ");
    scanf("%d", &zi);
    printf("Descrierea: ");
    scanf("%s", descriere);
    printf("Suma noua: ");
    scanf("%f", &suma_noua);
    printf("Tipul nou: ");
    scanf("%s", tip_nou);
    printf("Descrierea noua: ");
    scanf("%s", descriere_noua);
    if(validareTranzactie(zi, suma_noua, tip_nou, descriere_noua) == 1)
    {
        modificaTranzactie_service(service, zi, descriere, suma_noua, tip_nou, descriere_noua);
        printf("Tranzactie modificata cu succes!\n");
    }
    else
    {
        printf("Tranzactie invalida!\n");
    }
    free(tip_nou);
    free(descriere);
    free(descriere_noua);
}

/*
 * Functie care afiseaza toate tranzactiile
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile
 */
void afiseaza_tranzactii_ui(SERVICE *service) {
    printf("Toate tranzactiile:\n");
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        printf("%d\t%.2f\t%s\t%s\n", get_zi(tranzactie), get_suma(tranzactie), get_tip(tranzactie), get_descriere(tranzactie));
    }
    printf("----------------------------------\n");
}

/*
 * Functie care afiseaza toate tranzactiile de un anumit tip
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile de un anumit tip
 */
void afiseaza_tranzactii_ui_tip(SERVICE *service) {
    char *tip = (char *) malloc(20 * sizeof(char));
    printf("Tipul: ");
    scanf("%s", tip);
    if (strcmp(tip, "intrare") != 0 && strcmp(tip, "iesire") != 0) {
        printf("Tip invalid!\n");
        return;
    }
    printf("\n");
    printf("Tranzactiile de tipul %s:\n", tip);
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        if (strcmp(get_tip(tranzactie), tip) == 0) {
            printf("%d\t%.2f\t%s\t%s\n", get_zi(tranzactie), get_suma(tranzactie), get_tip(tranzactie), get_descriere(tranzactie));
        }
    }
    printf("----------------------------------\n");
    free(tip);
}

/*
 * Functie care afiseaza toate tranzactiile cu suma mai mare decat o valoare
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile cu suma mai mare decat o valoare
 */
void afiseaza_tranzactii_ui_suma_mai_mare(SERVICE *service) {
    float suma;
    printf("Suma: ");
    scanf("%f", &suma);
    printf("\n");
    printf("Tranzactiile cu suma mai mare decat %.2f:\n", suma);
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        if (get_suma(tranzactie) > suma) {
            printf("%d\t%.2f\t%s\t%s\n", get_zi(tranzactie), get_suma(tranzactie), get_tip(tranzactie), get_descriere(tranzactie));
        }
    }
    printf("----------------------------------\n");

}

/*
 * Functie care afiseaza toate tranzactiile cu suma mai mica decat o valoare
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile cu suma mai mica decat o valoare
 */
void afiseaza_tranzactii_ui_suma_mai_mica(SERVICE *service) {
    float suma;
    printf("Suma: ");
    scanf("%f", &suma);
    printf("\n");
    printf("Tranzactiile cu suma mai mica decat %.2f:\n", suma);
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for (int i = 0; i < getNrTranzactii(service->repo); i++) {
        Tranzactie *tranzactie = cautaTranzactie(service->repo, i);
        if (get_suma(tranzactie) < suma) {
            printf("%d\t%.2f\t%s\t%s\n", get_zi(tranzactie), get_suma(tranzactie), get_tip(tranzactie), get_descriere(tranzactie));
        }
    }
    printf("----------------------------------\n");

}

/*
 * Functie care compara doua tranzactii dupa suma
 * @param a - const void*
 * @param b - const void*
 * @return int
 * preconditii: a, b - const void*
 * postconditii: se returneaza -1 daca suma tranzactiei a este mai mica decat suma tranzactiei b,
 * 1 daca suma tranzactiei a este mai mare decat suma tranzactiei b,
 * 0 daca sunt egale
 */
int compara_suma(const void* a, const void* b) {
    Tranzactie* tranzactie1 = (Tranzactie*)a;
    Tranzactie* tranzactie2 = (Tranzactie*)b;
    if (get_suma(tranzactie1) < get_suma(tranzactie2)) {
        return -1;
    } else if (get_suma(tranzactie1) > get_suma(tranzactie2)) {
        return 1;
    } else {
        return 0;
    }
}

/*
 * Functie care afiseaza toate tranzactiile ordonate dupa suma (crescator)
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile ordonate dupa suma (crescator)
 */
void afiseaza_tranzactii_ui_suma_crescator(SERVICE* service){
    int nr_tranzactii = getNrTranzactii(service->repo);
    Tranzactie* tranzactii = malloc(nr_tranzactii * sizeof(Tranzactie));
    for(int i = 0; i < nr_tranzactii; i++){
        tranzactii[i] = *cautaTranzactie(service->repo, i);
    }
    qsort(tranzactii, nr_tranzactii, sizeof(Tranzactie), compara_suma);
    printf("Tranzactiile sortate crescator dupa suma:\n");
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for(int i = 0; i < nr_tranzactii; i++){
        printf("%d\t%.2f\t%s\t%s\n", get_zi(&tranzactii[i]), get_suma(&tranzactii[i]), get_tip(&tranzactii[i]), get_descriere(&tranzactii[i]));
    }
    printf("----------------------------------\n");
    free(tranzactii);
}

/*
 * Functie care afiseaza toate tranzactiile ordonate dupa suma (descrescator)
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile ordonate dupa suma (descrescator)
 */
void afiseaza_tranzactii_ui_suma_descrescator(SERVICE* service){
    int nr_tranzactii = getNrTranzactii(service->repo);
    Tranzactie* tranzactii = malloc(nr_tranzactii * sizeof(Tranzactie));
    for(int i = 0; i < nr_tranzactii; i++){
        tranzactii[i] = *cautaTranzactie(service->repo, i);
    }
    qsort(tranzactii, nr_tranzactii, sizeof(Tranzactie), compara_suma);
    printf("Tranzactiile sortate crescator dupa suma:\n");
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for(int i = nr_tranzactii - 1; i >= 0; i--){
        printf("%d\t%.2f\t%s\t%s\n", get_zi(&tranzactii[i]), get_suma(&tranzactii[i]), get_tip(&tranzactii[i]), get_descriere(&tranzactii[i]));
    }
    printf("----------------------------------\n");
    free(tranzactii);
}

/*
 * Functie care compara doua tranzactii dupa zi
 * @param a - const void*
 * @param b - const void*
 * @return int
 * preconditii: a, b - const void*
 * postconditii: se returneaza -1 daca ziua tranzactiei a este mai mica decat ziua tranzactiei b,
 * 1 daca ziua tranzactiei a este mai mare decat ziua tranzactiei b,
 * 0 daca sunt egale
 */
int compara_zi(const void* a, const void* b) {
    Tranzactie* tranzactie1 = (Tranzactie*)a;
    Tranzactie* tranzactie2 = (Tranzactie*)b;
    if (get_zi(tranzactie1) < get_zi(tranzactie2)) {
        return -1;
    } else if (get_zi(tranzactie1) > get_zi(tranzactie2)) {
        return 1;
    } else {
        return 0;
    }
}

/*
 * Functie care afiseaza toate tranzactiile ordonate dupa zi (crescator)
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile ordonate dupa zi (crescator)
 */
void afiseaza_tranzactii_ui_zi_crescator(SERVICE* service){
    int nr_tranzactii = getNrTranzactii(service->repo);
    Tranzactie* tranzactii = malloc(nr_tranzactii * sizeof(Tranzactie));
    for(int i = 0; i < nr_tranzactii; i++){
        tranzactii[i] = *cautaTranzactie(service->repo, i);
    }
    qsort(tranzactii, nr_tranzactii, sizeof(Tranzactie), compara_zi);
    printf("Tranzactiile sortate crescator dupa zi:\n");
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for(int i = 0; i < nr_tranzactii; i++){
        printf("%d\t%.2f\t%s\t%s\n", get_zi(&tranzactii[i]), get_suma(&tranzactii[i]), get_tip(&tranzactii[i]), get_descriere(&tranzactii[i]));
    }
    printf("----------------------------------\n");
    free(tranzactii);
}

/*
 * Functie care afiseaza toate tranzactiile ordonate dupa zi (descrescator)
 * service - pointer la un obiect de tip SERVICE
 * preconditii: service - pointer la un obiect de tip SERVICE
 * postconditii: se afiseaza toate tranzactiile ordonate dupa zi (descrescator)
 */
void afiseaza_tranzactii_ui_zi_descrescator(SERVICE* service){
    int nr_tranzactii = getNrTranzactii(service->repo);
    Tranzactie* tranzactii = malloc(nr_tranzactii * sizeof(Tranzactie));
    for(int i = 0; i < nr_tranzactii; i++){
        tranzactii[i] = *cautaTranzactie(service->repo, i);
    }
    qsort(tranzactii, nr_tranzactii, sizeof(Tranzactie), compara_zi);
    printf("Tranzactiile sortate descrescator dupa zi:\n");
    printf("Ziua\tSuma\tTip\tDescriere\n");
    printf("----------------------------------\n");
    for(int i = nr_tranzactii - 1; i >= 0; i--){
        printf("%d\t%.2f\t%s\t%s\n", get_zi(&tranzactii[i]), get_suma(&tranzactii[i]), get_tip(&tranzactii[i]), get_descriere(&tranzactii[i]));
    }
    printf("----------------------------------\n");
    free(tranzactii);
}

/*
 * Functie care ruleaza interfata de utilizator
 * preconditii: -
 * postconditii: se ruleaza interfata de utilizator
 */
void run_ui() {
    REPO *repo = creeazaRepo();
    SERVICE *service = creeazaService(repo);




    int optiune, rezultat;
    do {
        printf("1. Adauga tranzactie\n");
        printf("2. Sterge tranzactie\n");
        printf("3. Modifica tranzactie\n");
        printf("4. Afiseaza tranzactii\n");
        printf("5. Afiseaza tranzactii de un anumit tip (intrare/iesire) \n");
        printf("6. Afiseaza tranzactii cu suma mai MARE decat o valoare\n");
        printf("7. Afiseaza tranzactii cu suma mai MICA decat o valoare\n");
        printf("8. Afiseaza tranzactii ordonate dupa suma (crescator)\n");
        printf("9. Afiseaza tranzactii ordonate dupa suma (descrescator)\n");
        printf("10. Afiseaza tranzactii ordonate dupa zi (crescator)\n");
        printf("11. Afiseaza tranzactii ordonate dupa zi (descrescator)\n");
        printf("0. Iesire\n");
        printf("Optiune: ");
        rezultat = scanf("%d", &optiune);
        while (getchar() != '\n');
        if (rezultat == 0) {
            printf("Optiune invalida\n");
            continue;
        }
        switch (optiune) {
            case 1:
                adauga_tranzactie_ui(service);
                break;
            case 2:
                sterge_tranzactie_ui(service);
                break;
            case 3:
                modifica_tranzactie_ui(service);
                break;
            case 4:
                afiseaza_tranzactii_ui(service);
                break;
            case 5:
                afiseaza_tranzactii_ui_tip(service);
                break;
            case 6:
                afiseaza_tranzactii_ui_suma_mai_mare(service);
                break;
            case 7:
                afiseaza_tranzactii_ui_suma_mai_mica(service);
                break;
            case 8:
                afiseaza_tranzactii_ui_suma_crescator(service);
                break;
            case 9:
                afiseaza_tranzactii_ui_suma_descrescator(service);
                break;
            case 10:
                afiseaza_tranzactii_ui_zi_crescator(service);
                break;
            case 11:
                afiseaza_tranzactii_ui_zi_descrescator(service);
                break;
            case 0:
                printf("CEAW\n");
                break;
            default:
                printf("Optiune invalida\n");
                printf("\n");
        }
    } while (optiune != 0);
    distrugeService(service);
    distrugeRepo(repo);
}
#include "tests.h"

void run_all_tests(){
    test_creeazaTranzactie();
    test_get_zi();
    test_get_suma();
    test_get_tip();
    test_get_descriere();
    test_set_zi();
    test_set_suma();
    test_set_tip();
    test_set_descriere();

    test_creeazaRepo();
    test_distrugeRepo();
    test_adaugaTranzactie();
    test_stergeTranzactie();
    test_modificaTranzactie();
    test_cautaTranzactie();
    test_getNrTranzactii();
    test_get_vector_tranzactii();

    test_creeazaService();
    test_distrugeService();
    test_adaugaTranzactie_service();
    test_stergeTranzactie_service();
    test_modificaTranzactie_service();

    test_validari();

}
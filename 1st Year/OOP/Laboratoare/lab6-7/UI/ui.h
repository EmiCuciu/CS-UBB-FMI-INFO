#ifndef LAB6_7_UI_H
#define LAB6_7_UI_H

#include "../Service/Service.h"
#include <iostream>
#include "../Validari/Validari.h"

class UI {
private:
    Service service; // referinta catre service

    /*
     * Afiseaza meniul
     */
    static void printMenu();

    /*
     * Returneaza un string citit de la tastatura
     * @return caracterele citite de la tastatura
     */
    static char getUserInput();

    /*
     * Afiseaza toate cartile
     */
    void printAll();

    /*
     * Adauga o carte
     */
    void addCarte_UI();

    /*
     * Sterge o carte
     */
    void deleteCarte_UI();

    /*
     * Modifica o carte
     */
    void modifyCarte_UI();

    /*
     * Cauta o carte
     */
    void findCarte_UI();

    /*
     * Adauga predefinite
     */
    void addPredefinite();

public:
    /*
     * Constructor de clasa UI
     * @param service - referinta catre service
     */
    explicit UI(Service &service);

    /*
     * Destructor de clasa UI
     */
    ~UI() = default;

    /*
     * Ruleaza aplicatia
     */
    void run();

};

#endif //LAB6_7_UI_H

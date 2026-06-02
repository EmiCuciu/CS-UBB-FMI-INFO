//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_UI_H
#define LAB8_UI_H

#include "../Service/Service.h"



class UI{
private:
    Service &service;

    static void printMenu();

    static void printCosMenu();

    static char getUserInput();

    void uiPrintCarti();

    void addPredef();

    void uiAddCarte();

    void uiUpdateCarte();

    void uiDeleteCarte();

    void uiCautaCarte();

    void uiFiltreazaCarti();

    void uiSorteazaCarti();

    void uiCosMenu();

    void uiPrintCos();

    void uiAddCarteCos();

    void uiDeleteCos();

    void uiPopulateRandomCos();

    void uiExportCos();

    void uinrCartiGen();

    /// undo
    void uiUndo();

public:
    explicit UI(Service &service);

    void run();
};

#endif //LAB8_UI_H

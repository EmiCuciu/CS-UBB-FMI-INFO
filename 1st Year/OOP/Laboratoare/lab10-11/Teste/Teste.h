//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_TESTE_H
#define LAB8_TESTE_H

#include "string"
#include "../Repository/Repo.h"


class Teste {
private:
    std::string titlu;
    std::string autor;
    std::string gen;
    int an;

    std::string titlu2;
    std::string autor2;
    std::string gen2;
    int an2;

    void testCarte();

    void testValidari();

    void testRepo();

    void testService();

public:
    Teste();

    void runAllTests();
};


#endif //LAB8_TESTE_H

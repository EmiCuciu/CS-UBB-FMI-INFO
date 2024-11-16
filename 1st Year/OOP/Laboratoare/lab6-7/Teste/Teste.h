#ifndef LAB6_7_TESTE_H
#define LAB6_7_TESTE_H

#include <string>

#include "../Domain/Carte.h"
#include "../Repository/Repository.h"
#include "../Service/Service.h"
#include "../Validari/validari.h"

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

    void testDomain();

    void testRepository();

    void testService();


public:
    Teste();

    void runTests();
};

#endif //LAB6_7_TESTE_H

#include "Teste.h"
#include <assert.h>
#include <iostream>

Teste::Teste() {
    titlu = "Mara";
    autor = "Ioan Slavici";
    gen = "Roman realist";
    an = 1922;

    titlu2 = "Ion";
    autor2 = "Liviu Rebreanu";
    gen2 = "Roman Interbelic";
    an2 = 1920;
}

void Teste::testDomain() {
    Carte carte1(titlu, autor, gen, an);
    Carte carte2(titlu2, autor2, gen2, an2);

    assert(carte1.getTitlu() == titlu);
    assert(carte1.getAutor() == autor);
    assert(carte1.getGen() == gen);
    assert(carte1.getAn() == an);

    assert(carte1.intoString() == "Mara\tIoan Slavici\tRoman realist\t1922");

    assert(carte2.getTitlu() == titlu2);
    assert(carte2.getAutor() == autor2);
    assert(carte2.getGen() == gen2);
    assert(carte2.getAn() == an2);

    assert(carte2.intoString() == "Ion\tLiviu Rebreanu\tRoman Interbelic\t1920");

    carte1.setTitlu(titlu2);
    carte1.setAutor(autor2);
    carte1.setGen(gen2);
    carte1.setAn(an2);

    assert(carte1.getTitlu() == titlu2);
    assert(carte1.getAutor() == autor2);
    assert(carte1.getGen() == gen2);
    assert(carte1.getAn() == an2);

}

void Teste::testRepository() {
    Repository repo;
    Carte carte1(titlu, autor, gen, an);
    Carte carte2(titlu2, autor2, gen2, an2);

    repo.addCarte(carte1);
    assert(repo.getSize() == 1);
    repo.addCarte(carte2);
    assert(repo.getSize() == 2);

    auto position = repo.findCarte(titlu2);
    assert(repo.validatePosition(position));
    repo.deleteCarte(position);
    assert(repo.getSize() == 1);

    position = repo.findCarte(titlu);
    repo.modifyCarte(position, carte2);
    assert(repo.getSize() == 1);

    assert(repo.getCarti()[0].getTitlu() == titlu2);
    assert(repo.getCarti()[0].getAutor() == autor2);
    assert(repo.getCarti()[0].getGen() == gen2);
    assert(repo.getCarti()[0].getAn() == an2);
}

void Teste::testService() {
    Repository repo;
    Service service(repo);

    service.addCarte(titlu, autor, gen, an);
    assert(service.getCarti().size() == 1);
    assert(repo.getSize() == 1);

    service.addCarte(titlu, autor, gen, an);
    assert(service.getCarti().size() == 1);

    service.addCarte(titlu2, autor2, gen2, an2);
    assert(service.getCarti().size() == 2);
    assert(repo.getSize() == 2);

    service.modifyCarte(titlu, autor2, gen2, an2);
    assert(service.getCarti()[0].getTitlu() == titlu);
    assert(service.getCarti()[0].getAutor() == autor2);
    assert(service.getCarti()[0].getGen() == gen2);
    assert(service.getCarti()[0].getAn() == an2);

    service.modifyCarte("Nonexistent title", autor2, gen2, an2);
    assert(service.getCarti()[0].getTitlu() == titlu);  // the first book should still be the same

    service.deleteCarte(titlu);
    assert(service.getCarti().size() == 1);
    assert(repo.getSize() == 1);

    service.deleteCarte("Nonexistent title");
    assert(service.getCarti().size() == 1);  // size should still be 1

    auto lista = service.findCarte("M");
    assert(lista.empty());

    lista = service.findCarte("I");
    assert(lista.size() == 1);
}


void Teste::runTests() {
    testDomain();
    testRepository();
    testService();
    std::cout << "All tests passed\n";
}
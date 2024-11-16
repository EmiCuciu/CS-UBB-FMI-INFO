//
// Created by Emi on 4/14/2024.
//

#include "Teste.h"
#include "../Service/Service.h"
#include "../Validari/Validari.h"
#include <cassert>
#include <fstream>

Teste::Teste() {
    const int YEAR = 1890;
    const int OTHER_YEAR = 1910;

    titlu = "War and Peace";
    autor = "Lev Tolstoy";
    gen = "roman";
    an = YEAR;

    titlu2 = "The brothers Karamazov";
    autor2 = "Fyodor Dostoevsky";
    gen2 = "literature";
    an2 = OTHER_YEAR;
}

void Teste::testCarte() {
    Carte new_book(titlu, autor, gen, an);

    assert(new_book.getTitlu() == titlu);
    assert(new_book.getAutor() == autor);
    assert(new_book.getGen() == gen);
    assert(new_book.getAn() == an);


    new_book.setTitlu(titlu2);
    new_book.setAutor(autor2);
    new_book.setGen(gen2);
    new_book.setAn(an2);

    assert(new_book.getTitlu() == titlu2);
    assert(new_book.getAutor() == autor2);
    assert(new_book.getGen() == gen2);
    assert(new_book.getAn() == an2);

    assert(new_book.toString() == titlu2 + "\t" + autor2 + "\t" + gen2 + "\t" + std::to_string(an2));

    //std::cout << "Domain tests ran successfully.\n";
}

void Teste::testRepo() {


    /*
     * TEST LIBRARY
     */


    Librarie repo;
    Carte new_book(titlu, autor, gen, an);
    Carte other_book(titlu2, autor2, gen2, an2);

    // TEST ADD

    repo.addCarte(new_book);
    assert(repo.getLen() == 1);
    repo.addCarte(other_book);
    assert(repo.getLen() == 2);

    // TEST DELETE

    auto book_iter = repo.findCarte(titlu);
    assert(book_iter != repo.getCarti().end());
    repo.deleteCarte(book_iter);
    assert(repo.getLen() == 1);

    // TEST UPDATE

    auto new_book_iter = repo.findCarte(titlu2);
    Librarie::updateCarte(new_book_iter, new_book);
    assert(repo.getLen() == 1);

    auto all = repo.getCarti();
    assert(all[0].getTitlu() == titlu);
    assert(all[0].getAutor() == autor);
    assert(all[0].getGen() == gen);
    assert(all[0].getAn() == an);

    /*
     * TEST SHOPPING CART
     */

    Cos cart;

    // TEST DELETE ALL

    cart.addCarte(new_book);
    cart.addCarte(other_book);
    assert(cart.getLen() == 2);

    cart.deleteAllBooks();
    assert(cart.getLen() == 0);

    //std::cout << "Repository tests ran successfully.\n";
}

void Teste::testValidari() {
    Carte good_book(titlu, autor, gen, an);
    Validator::valCarte(good_book);

    const int BAD_YEAR = 2050;
    std::string bad_title = "war and peace";
    std::string bad_author = "levTolstoy";
    std::string bad_genre = "aandomWORDS";
    int bad_year = BAD_YEAR;

    Carte bad_book(bad_title, bad_author, bad_genre, bad_year);

    //std::cout << "Validator tests ran successfully.\n";
}

void Teste::testService() {
    Librarie repo;
    Cos cart;
    Service service(repo, cart);

    // TEST ADD

    service.addCarteLibrarie(titlu, autor, gen, an);
    assert(service.getAllLib().size() == 1);
    assert(repo.getLen() == 1);
    try {
        service.addCarteLibrarie(titlu, autor2,gen2,an2);
        assert(true);
    } catch (const ServiceException &e) {
        assert(service.getAllLib().size() == 1);
        assert(repo.getLen() == 1);
    }

    // TEST UPDATE

    service.updateCarteLibrarie(titlu,autor2,gen2,an2);
    assert(service.getAllLib().size() == 1);

    auto all_repo = repo.getCarti();
    assert(all_repo[0].getTitlu() == titlu);
    assert(all_repo[0].getAutor() == autor2);
    assert(all_repo[0].getGen() == gen2);
    assert(all_repo[0].getAn() == an2);

    try {
        service.updateCarteLibrarie(titlu2,autor,gen,an);
        assert(true);
    } catch (const ServiceException &e) {
        assert(service.getAllLib().size() == 1);

        auto all = service.getAllLib();
        assert(all[0].getTitlu() == titlu);
        assert(all[0].getAutor() == autor2);
        assert(all[0].getGen() == gen2);
        assert(all[0].getAn() == an2);
    }

    // TEST DELETE

    service.deleteCarteLibrarie(titlu);
    assert(service.getAllLib().empty());

    try {
        service.addCarteLibrarie(titlu, autor2, gen2,an2);
        service.deleteCarteLibrarie(autor2);
        assert(true);
    } catch (const ServiceException &e) {
        assert(service.getAllLib().size() == 1);
    }

    // TEST FIND

    auto list = service.cautaCarteLibrarie(titlu2);
    assert(list.empty());

    list = service.cautaCarteLibrarie(titlu);
    assert(list.size() == 1);

    // TEST FILTER

    const int GOOD_MIN_YEAR = 1900;
    const int BAD_MIN_YEAR = 2000;

    auto filter = service.filtreazaCarti(GOOD_MIN_YEAR);
    assert(filter.size() == 1);

    filter = service.filtreazaCarti(BAD_MIN_YEAR);
    assert(filter.empty());

    // TEST SORT

    service.deleteCarteLibrarie(titlu);
    service.addCarteLibrarie(titlu2,autor2,gen2,an2);
    service.addCarteLibrarie(titlu, autor, gen, an);auto sorted = service.sorteazaCarti([](const Carte &b1, const Carte &b2) {
        return b1.getAn() < b2.getAn();
    });

    assert(sorted.size() == 2);
    assert(sorted[0].getTitlu() == titlu);
    assert(sorted[0].getAutor() == autor);
    assert(sorted[0].getGen() == gen);
    assert(sorted[0].getAn() == an);

    // TEST ADD CART

    service.addCarteCos(titlu);
    assert(service.getAllCos().size() == 1);

    service.addCarteCos(titlu2);
    assert(service.getAllCos().size() == 2);

    std::string nonexistent_book = "Metamorphosis";
    try {
        service.addCarteCos(nonexistent_book);
        assert(true);
    } catch (const std::exception &e) {
        assert(cart.getLen() == 2);
        assert(service.getAllCos().size() == 2);
    }

    // TEST DELETE CART

    service.deleteCos();
    assert(service.getAllCos().empty());

    // TEST POPULATE RANDOM

    const int number = 12;
    service.populateRandomCos(number);
    assert(service.getAllCos().size() == number);

    service.populateRandomCos(number);
    assert(service.getAllCos().size() == number * 2);

    service.deleteCos();
    service.deleteCarteLibrarie(titlu);
    service.deleteCarteLibrarie(titlu2);

    try {
        service.populateRandomCos(number);
        assert(true);
    } catch (const std::exception &e) {
        assert(service.getAllCos().empty());
    }

    // TEST EXPORT

    service.deleteCos();

    service.addCarteLibrarie("Titlu1", "Autor1", "Gen1", 2000);
    service.addCarteLibrarie("Titlu2", "Autor2", "Gen2", 2001);
    service.addCarteLibrarie("Titlu3", "Autor3", "Gen3", 2002);

    service.addCarteCos("Titlu1");
    service.addCarteCos("Titlu2");
    service.addCarteCos("Titlu3");

    std::string filename = "test_export.html";
    service.exportCos(filename);

    std::ifstream file(filename);
    assert(file.good());

    // TEST NUMBER OF BOOKS PER GENRE

    std::string genre = "Gen1";
    auto map = service.numarCartiGen(genre);
    assert(map[genre] == 1);


    // TEST UNDO

    Librarie other_repo;
    Cos other_cart;
    Service other_service(other_repo, other_cart);

    other_service.addCarteLibrarie("Titlu1", "Autor1", "Gen1", 2000);
    other_service.addCarteLibrarie("Titlu2", "Autor2", "Gen2", 2001);
    assert(other_service.getAllLib().size() == 2);
    other_service.undo();
    assert(other_service.getAllLib().size() == 1);

    other_service.deleteCarteLibrarie("Titlu1");
    assert(other_service.getAllLib().empty());
    other_service.undo();
    assert(other_service.getAllLib().size() == 1);

    other_service.updateCarteLibrarie("Titlu1", "Autor3", "Gen3", 2003);
    other_service.undo();
    assert(other_service.getAllLib().size() == 1);
    assert(other_service.getAllLib()[0].getAutor() == "Autor1");
    assert(other_service.getAllLib()[0].getGen() == "Gen1");
    assert(other_service.getAllLib()[0].getAn() == 2000);
    assert(other_service.getAllLib()[0].getTitlu() == "Titlu1");

    other_service.undo();
    assert(other_service.getAllLib().empty());

    try {
        other_service.undo();
        assert(true);
    } catch (const ServiceException &e) {
        assert(other_service.getAllLib().empty());
    }



    //std::cout << "Service tests ran successfully.\n";
}



void Teste::runAllTests() {
    testCarte();
    testValidari();
    testRepo();
    testService();
    //std::cout << "All tests ran successfully.\n";
}

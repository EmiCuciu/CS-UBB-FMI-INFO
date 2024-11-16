//
// Created by Emi on 4/14/2024.
//

#include "Service.h"
#include "algorithm"
#include "utility"
#include "random"
#include "iostream"

Service::Service(Librarie &lib, Cos &cos) : librarie(lib), cos(cos) {}

std::vector<Carte> &Service::getAllLib() {
    return librarie.getCarti();
}

std::vector<Carte> &Service::getAllCos() {
    return cos.getCarti();
}

void Service::addCarteLibrarie(const std::string &titlu, const std::string &autor, const std::string &gen, int an) {
    auto position = librarie.findCarte(titlu);

    if (librarie.isValid(position)) {
        throw ServiceException("Exista deja o carte cu acest titlu.\n");
    }

    Carte new_carte(titlu, autor, gen, an);
    Validator::valCarte(new_carte);
    librarie.addCarte(new_carte);
    history.push_back(std::make_unique<UndoAdauga>(librarie, new_carte));
}

void Service::updateCarteLibrarie(const std::string &titlu, const std::string &new_autor, const std::string &new_gen,
                                  int new_an) {
    auto position = librarie.findCarte(titlu);

    if (!librarie.isValid(position)) {
        throw ServiceException("Nu exista carti cu acest titlu.\n");
    }

    Carte new_carte(titlu, new_autor, new_gen, new_an);
    Validator::valCarte(new_carte);
    auto updated_book = Librarie::updateCarte(position, new_carte);
    history.push_back(std::make_unique<UndoUpdate>(librarie, updated_book));
}

void Service::deleteCarteLibrarie(const std::string &titlu) {
    auto position = librarie.findCarte(titlu);

    if (!librarie.isValid(position)) {
        throw ServiceException("Nu exista carti cu acest titlu.\n");
    }
    auto deleted_book = librarie.deleteCarte(position);
    history.push_back(std::make_unique<UndoSterge>(librarie, deleted_book));
}

std::vector<Carte> Service::cautaCarteLibrarie(const std::string &titlu) {
    std::vector<Carte> found;
    std::vector<Carte> allBooks = librarie.getCarti();

    std::copy_if(allBooks.begin(),
                 allBooks.end(),
                 std::back_inserter(found),
                 [&titlu](const Carte &carte) {
        return carte.getTitlu().find(titlu) == 0;
    });
    return found;
}

std::vector<Carte> Service::filtreazaCarti(int an_minim) {
    std::vector<Carte> filtrate;
    std::vector<Carte> allBooks = librarie.getCarti();

    std::copy_if(allBooks.begin(), allBooks.end(), std::back_inserter(filtrate), [an_minim](const Carte &carte) {
        return carte.getAn() >= an_minim;
    });
    return filtrate;
}

std::vector<Carte> Service::sorteazaCarti(const std::function<bool(const Carte &, const Carte &)> &compare) {
    std::vector<Carte> sortate = librarie.getCarti();
    std::sort(sortate.begin(), sortate.end(), compare);
    return sortate;}

///Cos

void Service::addCarteCos(const std::string &titlu) {
    auto position = librarie.findCarte(titlu);

    if (!librarie.isValid(position)) {
        throw ServiceException("Nu sunt carti cu acest titlu.\n");
    }
    Validator::valCarte(*position);
    cos.addCarte(*position);
    notify();
}

void Service::deleteCos() {
    cos.deleteAllBooks();
    notify();
}

void Service::populateRandomCos(size_t nr_carti) {
    if (librarie.getLen() == 0) {
        throw ServiceException("Nu sunt carti in librarie.\n");
    }

    std::mt19937 gen(std::random_device{}());
    std::uniform_int_distribution<int> distribution(0, static_cast<int>(librarie.getLen() - 1));

    for (size_t i = 0; i < nr_carti; ++i){
        int random_number = distribution(gen);
        cos.addCarte(librarie.getCarti()[random_number]);
    }
    notify();
}

std::unordered_map<std::string, int>Service::numarCartiGen(std::string &gen){
    std::unordered_map<std::string, int> genCount;
    std::vector<Carte> allBooks = librarie.getCarti();

    for(const auto& carte : allBooks){
        if (carte.getGen() == gen)
        genCount[gen]++;
    }
    return genCount;
}

void Service::undo() {
    if(history.empty()){
        throw ServiceException("Nu se mai poate face undo.\n");
    }
    auto action = std::move(history.back());
    history.pop_back();

    action->doUndo();
}

std::unordered_map<std::string, int> Service::getRaport() {
    std::unordered_map<std::string, int> raport;

    for (const Carte &book : librarie.getCarti()) {
        raport[book.getGen()] += 1;
    }

    return raport;
}


void Service::addpredefinite(){
    addCarteLibrarie("TATA", "Mama", "SF", 2012);
    addCarteLibrarie("BAU", "BALAUR", "KALA", 1056);
    addCarteLibrarie("MAra", "Tara", "SF", 2021);
    addCarteLibrarie("Masdas", "Dasa", "KALA", 2000);
    addCarteLibrarie("ORAS", "Tara", "UBB", 2010);
}


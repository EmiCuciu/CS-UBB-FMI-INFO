//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_SERVICE_H
#define LAB8_SERVICE_H

#include <utility>
#include <memory>

#include "../Repository/Repo.h"
#include "../UNDO/undo.h"
#include "../Validari/Validari.h"
#include "../GUI/Observer.h"

class ServiceException : public std::exception {
private:
    std::string message;

public:
    explicit ServiceException(std::string message) : message(std::move(message)) {} [[nodiscard]] const char *what() const noexcept override { return message.c_str(); }


};

class Service : public Subject{
private:
    Librarie &librarie;
    Cos &cos;

    std::vector<std::unique_ptr<ActiuneUndo>> history;

public:
    Service(Librarie &lib, Cos &cos);

    std::vector<Carte> &getAllLib();

    std::vector<Carte> &getAllCos();

    ///Lib
    void addCarteLibrarie(const std::string &titlu, const std::string &autor, const std::string &gen, int an);

    void
    updateCarteLibrarie(const std::string &titlu, const std::string &new_autor, const std::string &new_gen, int new_an);

    void deleteCarteLibrarie(const std::string &titlu);

    std::vector<Carte> cautaCarteLibrarie(const std::string &titlu);

    std::vector<Carte> filtreazaCarti(int an_minim);

    std::vector<Carte> sorteazaCarti(const std::function<bool(const Carte &, const Carte &)> &compare);

    ///Cos
    void addCarteCos(const std::string &titlu);

    void deleteCos();

    void populateRandomCos(size_t nr_carti);

    void exportCos(const std::string &filename);

    std::unordered_map<std::string, int> numarCartiGen(std::string &gen);

    void undo();

    void addpredefinite();

    std::unordered_map<std::string, int> getRaport();

};



#endif //LAB8_SERVICE_H

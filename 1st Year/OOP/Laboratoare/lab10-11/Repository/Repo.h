//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_REPO_H
#define LAB8_REPO_H

#include <utility>

#include "../Domain/Carte.h"
#include "functional"
#include "vector"

///Generic Repo

class AbstractREPO {
public:
    std::vector<Carte> items;

    AbstractREPO() = default;

    virtual void addCarte(const Carte &carte) = 0;

    virtual std::vector<Carte> & getCarti() = 0;

    [[nodiscard]] virtual size_t getLen() const = 0;
};

/// Libraria Repo

class Librarie : public AbstractREPO {
public:

    void addCarte(const Carte &carte) override;

    std::vector<Carte> &getCarti() override;

    [[nodiscard]] size_t getLen() const override;

    Carte deleteCarte(std::vector<Carte>::iterator &position);

    static Carte updateCarte(std::vector<Carte>::iterator &position, const Carte &new_carte);

    std::vector<Carte>::iterator findCarte(const std::string &titlu);

    [[nodiscard]] bool isValid(std::vector<Carte>::iterator &iterator) const;
};

/// Cosul Repo

class Cos : public AbstractREPO{
public:

    void addCarte(const Carte &carte) override;

    std::vector<Carte> &getCarti() override;

    [[nodiscard]] size_t getLen() const override;

    void deleteAllBooks();

    [[nodiscard]] bool isValid(std::vector<Carte>::iterator &iterator) const;
};



#endif //LAB8_REPO_H

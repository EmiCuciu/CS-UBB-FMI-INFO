#ifndef REPOSITORY_H
#define REPOSITORY_H

#include "domain.h"
#include <vector>
#include <string>

class Repository {
private:
    std::vector<Produs> produse;
    std::string fisier;

    void citesteProduse();
    void salveazaProduse();

public:
    Repository(const std::string &fisier);

    std::vector<Produs> getProduse() const;
    void adaugaProdus(const Produs &produs);
};

#endif // REPOSITORY_H

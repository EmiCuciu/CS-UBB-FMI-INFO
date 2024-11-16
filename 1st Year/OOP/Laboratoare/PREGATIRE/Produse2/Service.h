#ifndef SERVICE_H
#define SERVICE_H

#include "domain.h"
#include "Repository.h"
#include <vector>
#include <unordered_map>
#include <string>

class ServiceException : public std::exception {
private:
    std::string message;

public:
    explicit ServiceException(const std::string &message);
    const char* what() const noexcept override;
};

class Service {
private:
    Repository repo;
    std::unordered_map<std::string, int> tipuriCount;

    void validateProdus(const Produs &produs);

public:
    Service(const std::string &fisier);

    std::vector<Produs> getProduse() const;
    std::vector<std::string> getTipuri() const;
    int getNumarProduseTip(const std::string &tip) const;

    void adaugaProdus(int id, const std::string &nume, const std::string &tip, double pret);
};

#endif // SERVICE_H

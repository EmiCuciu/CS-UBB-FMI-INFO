#ifndef SERVICE_H
#define SERVICE_H

#include "repository.h"
#include "validari.h"
#include <unordered_map>
#include <string>

class ServiceException : public std::exception {
private:
    std::string msg;

public:
    explicit ServiceException(const std::string& msg);
    const char* what() const noexcept override;
};

class Service {
private:
    Repository& repository;

public:
    explicit Service(Repository& repository);

    void addProduct(int id, const std::string& nume, const std::string& tip, double pret);
    void deleteProduct(int id);
    const std::vector<Product>& getProducts() const;
    std::vector<Product> sorteaza() const;
    std::unordered_map<std::string, int> NumarTip() const;
};

#endif // SERVICE_H

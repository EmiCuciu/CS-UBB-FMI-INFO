//
// Created by Emi on 5/22/2024.
//

#ifndef UNTITLED_SERVICE_H
#define UNTITLED_SERVICE_H

#include "Repository.h"
#include "Validari.h"


class ServiceException : std::exception {
private:
    std::string msg;

public:
    explicit ServiceException(std::string msg) : msg(std::move(msg)) {}

    [[nodiscard]] const char *what() const noexcept override {
        return msg.c_str();
    }
};

class Service{
private:
    Repository &repository;

public:
    explicit Service(Repository &repository) : repository(repository) {
        repository.loadFromFile();
    }

    void addService(int id, std::string& vg, std::string& vc, std::string& tip,  std::string& severity);

    void deleteService(int id);

    std::vector<Greseala> getService();

    std::vector<Greseala> sorteaza();


};

#endif //UNTITLED_SERVICE_H

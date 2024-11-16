//
// Created by Emi on 5/22/2024.
//

#ifndef UNTITLED_REPOSITORY_H
#define UNTITLED_REPOSITORY_H

#include <utility>

#include "Domain.h"
#include "vector"

class Repository{
private:
    std::vector<Greseala> items;
    std::string file_name;

public:
    explicit Repository(std::string file_name) : file_name(std::move(file_name)) {}

    void addGreseala(Greseala &greseala);

    void deleteGreseala(std::vector<Greseala>::iterator &pos);

    void loadFromFile();

    void writeToFile();

    size_t getSize();

    std::vector<Greseala> getGreseli();

    std::vector<Greseala>::iterator findGreseala(int id);

    bool validatePosition(std::vector<Greseala>::iterator &pos);


};

#endif //UNTITLED_REPOSITORY_H

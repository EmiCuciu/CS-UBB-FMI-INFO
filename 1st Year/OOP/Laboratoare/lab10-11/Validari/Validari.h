//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_VALIDARI_H
#define LAB8_VALIDARI_H

#include "../Domain/Carte.h"
#include "string"

class Validator_exception : public std::exception {
private:
    std::string message;

public:
    explicit Validator_exception(std::string message) : message(std::move(message)) {} [[nodiscard]] const char *what() const noexcept override { return message.c_str(); }
};

class Validator {
private:
    static bool valTitlu(const std::string &titlu);

    static bool valAn(int an);

public:
    static void valCarte(const Carte &carte);
};

#endif //LAB8_VALIDARI_H

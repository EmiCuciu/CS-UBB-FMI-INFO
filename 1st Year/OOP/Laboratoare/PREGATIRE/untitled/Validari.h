//
// Created by Emi on 5/22/2024.
//

#ifndef UNTITLED_VALIDARI_H
#define UNTITLED_VALIDARI_H

#include <utility>
#include "Domain.h"
#include "exception"
#include "string"

class ValidariException : std::exception {
private:
    std::string msg;

public:
    explicit ValidariException(std::string msg) : msg(std::move(msg)) {}

    [[nodiscard]] const char *what() const noexcept override {
        return msg.c_str();
    }
};

class Validari {
private:
    bool validateID(int id);

    bool validateVG(const std::string& ver_gresita);

    bool validateVC(const std::string& ver_corecta);

    bool validateTip(const std::string& tip);

    bool validateSeverity(std::string severity);

public:
    void validateGreseala(Greseala greseala);
};

#endif //UNTITLED_VALIDARI_H

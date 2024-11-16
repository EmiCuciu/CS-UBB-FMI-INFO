#ifndef VALIDARI_H
#define VALIDARI_H

#include "domain.h"
#include <string>

class ValidationException : public std::exception {
private:
    std::string msg;

public:
    explicit ValidationException(const std::string &msg);

    const char *what() const noexcept override;
};

class Validari {
public:
    void validateProduct(const Produs &product);
};

#endif // VALIDARI_H

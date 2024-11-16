#include "validari.h"

ValidationException::ValidationException(const std::string& msg) : msg(msg) {}

const char* ValidationException::what() const noexcept {
return msg.c_str();
}

void Validari::validateProduct(const Produs& product) {
    std::string errors;

    if (product.getNume().empty()) {
        errors.append("Numele produsului nu poate fi vid!\n");
    }

    if (product.getPret() < 1.0 || product.getPret() > 100.0) {
        errors.append("Pretul produsului trebuie sa fie intre 1.0 si 100.0!\n");
    }

    // Validare ID unic
    // Aceasta validare se face in Service, nu in Validari

    if (!errors.empty()) {
        throw ValidationException(errors);
    }
}

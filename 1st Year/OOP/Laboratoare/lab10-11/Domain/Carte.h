//
// Created by Emi on 4/14/2024.
//

#ifndef LAB8_CARTE_H
#define LAB8_CARTE_H

#include <string>

class Carte {
private:
    std::string titlu;
    std::string autor;
    std::string gen;
    int an = 0;

public:
    /// Default constructor
    Carte() = default;

    /// Constructor cu parametri
    Carte(std::string titlu, std::string autor, std::string gen, int an);

    /// Copy constructor
    Carte(const Carte &other);

    [[nodiscard]]std::string getTitlu() const;

    void setTitlu(const std::string &new_titlu);

    [[nodiscard]]std::string getAutor() const;

    void setAutor(const std::string &new_autor);

    [[nodiscard]]std::string getGen() const;

    void setGen(const std::string &new_gen);

    [[nodiscard]]int getAn() const;

    void setAn(int new_an);

    [[nodiscard]]std::string toString() const;
};

#endif //LAB8_CARTE_H

//
// Created by Emi on 5/16/2024.
//

#ifndef LAB4_P3_H
#define LAB4_P3_H

#include <fstream>
#include <iostream>
#include <map>
#include <queue>
#include <sstream>
#include <vector>




// Structura pentru un nod din arborele Huffman
struct Nod {
    char caracter;
    int frecventa;
    Nod *stanga, *dreapta;

    Nod(char caracter, int frecventa, Nod* stanga = nullptr, Nod* dreapta = nullptr)
            : caracter(caracter), frecventa(frecventa), stanga(stanga), dreapta(dreapta) {}
};

// Structura pentru a compara nodurile in functie de frecventa si caracter
struct Compara {
    bool operator()(Nod* a, Nod* b) {
        if (a->frecventa == b->frecventa) return a->caracter > b->caracter;
        return a->frecventa > b->frecventa;
    }
};

void codifica(Nod* radacina, const std::string& str,
              std::map<char, std::string>& huffman);

void Huffman(const std::string& text);

void p3();

#endif //LAB4_P3_H

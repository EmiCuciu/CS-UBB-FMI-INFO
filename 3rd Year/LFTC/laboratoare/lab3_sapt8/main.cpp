#include <iostream>
#include <fstream>
#include <cstdio>

#include "HashSymbolTable.h"
#include "token.h"

// Aceste variabile sunt definite in 'scanner.l' (si in 'lex.yy.c' generat)
extern HashSymbolTable ts;
extern std::vector<std::pair<int, int> > fip;

// Functia principala a analizatorului, generat de Flex
extern int yylex();

extern FILE *yyin;


void save_fip(const std::string &filename) {
    std::ofstream out(filename);

    if (!out.is_open()) {
        std::cerr << "Eroare: Nu s-a putut deschide fisierul FIP: " << filename << std::endl;
        return;
    }

    out << "COD\tPos_TS\n";
    out << "-----------------\n";

    for (const auto &pair: fip) {
        out << pair.first << "\t" << pair.second << "\n";
    }
    out.close();
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        std::cerr << "Utilizare: ./analizor <fisier_sursa>\n";
        return 1;
    }

    std::string input_filename = argv[1];

    yyin = fopen(input_filename.c_str(), "r");

    if (!yyin) {
        std::cerr << "Eroare: Nu s-a putut deschide fisierul de intrare " << input_filename << "\n";
        return 1;
    }

    std::cout << "Se analizeaza fisierul: " << input_filename << " ...\n";

    //pornim analizatorul
    yylex();

    fclose(yyin);

    ts.save_to_file("ts.txt");
    save_fip("fip.txt");

    std::cout << "Analiza lexicala finalizata.\n";
    std::cout << "TS salvat in: ts.txt\n";
    std::cout << "FIP salvat in: fip.txt\n";

    return 0;
}

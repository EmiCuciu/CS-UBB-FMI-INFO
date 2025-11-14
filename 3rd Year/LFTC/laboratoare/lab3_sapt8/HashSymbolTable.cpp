//
// Created by emicuciu on 11/14/25.
//

#include "HashSymbolTable.h"

#include <fstream>
#include <iostream>

HashSymbolTable::HashSymbolTable() = default;

int HashSymbolTable::insert(const string &token, const string &tip) {
    auto iterator = token_to_pos.find(token);

    if (iterator != token_to_pos.end()) {
        return iterator->second; // daca exista returnam pozitia lui
    }

    const int pos = entries.size();

    entries.emplace_back(token, tip);

    token_to_pos[token] = pos;

    return pos;
}

void HashSymbolTable::save_to_file(const string &path) {
    ofstream out(path);

    if (!out.is_open()) {
        cerr << "Eroare: Nu s-a putut deschide fisierul TS: " << path << endl;
        return;
    }

    out << "Pos_TS\tAtom_lexical\tTip\n";
    out << "------------------------------\n";

    for (size_t pos = 0; pos < entries.size(); ++pos) {
        const string &token = entries[pos].first;
        const string &tip = entries[pos].second;

        out << pos << "\t" << token << "\t" << tip << "\n";
    }

    out.close();
}



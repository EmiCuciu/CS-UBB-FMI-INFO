//
// Created by emicuciu on 11/14/25.
//

#ifndef LAB3_SAPT8_HASHSYMBOLTABLE_H
#define LAB3_SAPT8_HASHSYMBOLTABLE_H
#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

class HashSymbolTable {
private:
    vector<pair<string, string> > entries; // (token, tip)

    unordered_map<string, int> token_to_pos; // mapare token -> pozitie in entries

public:
    HashSymbolTable();

    int insert(const string &token, const string &tip);

    void save_to_file(const string &path);
};


#endif //LAB3_SAPT8_HASHSYMBOLTABLE_H

//
// Created by emicu on 10/27/2025.
//

#ifndef AUTOMATFINIT_H
#define AUTOMATFINIT_H
#include <map>
#include <set>
#include <string>

using namespace std;

class AutomatFinit {
public:
    set<string> states;
    set<char> alphabet;
    map<pair<string, char>, string> transitions;
    string startState;
    set<string> finalStates;

    void citireTastatura();
    void citireFisier(const string& filename);
    void afisareStari() const;
    void afisareAlfabet() const;
    void afisareTranzitii() const;
    void afisareStariFinale() const;
    bool verificaSecventa(const string& secventa);
    string celMaiLungPrefixAcceptat(const string& secventa);
};



#endif //AUTOMATFINIT_H

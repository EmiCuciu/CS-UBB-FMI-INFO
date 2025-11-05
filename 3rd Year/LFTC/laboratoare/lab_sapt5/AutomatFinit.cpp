//
// Created by emicu on 10/27/2025.
//

#include "AutomatFinit.h"

#include <fstream>
#include <iostream>
#include <ranges>
using namespace std;

    void AutomatFinit::citireTastatura() {
        int nrStates, nrFinalStates, nrTransitions;
        string state;

        cout << "Numar de stari: ";
        cin >> nrStates;
        cout << "Introduceti starile: ";
        for (int i = 0; i < nrStates; ++i) {
            cin >> state;
            states.insert(state);
        }

        int alphabetSize;
        cout << "Dimensiunea alfabetului: ";
        cin >> alphabetSize;
        cout << "Introduceti simbolurile alfabetului: ";
        for (int i = 0; i < alphabetSize; ++i) {
            char symbol;
            cin >> symbol;
            alphabet.insert(symbol);
        }

        cout << "Numar de tranzitii: ";
        cin >> nrTransitions;
        cout << "Introduceti tranzitiile (stare curenta, simbol, stare urmatoare): " << endl;
        for (int i = 0; i < nrTransitions; ++i) {
            string currentState, nextState;
            char symbol;
            cin >> currentState >> symbol >> nextState;

            if (transitions.contains( {currentState, symbol} )) {
                cerr << "Error: Automatul nu este determinist!\n" << "Tranzitie nedeterminista detectata la starea " << currentState << " cu simbolul " << symbol << "." << endl;
                return;
            }

            transitions[{currentState, symbol}] = nextState;
        }

        cout << "Starea initiala: ";
        cin >> startState;

        cout << "Numar de stari finale: ";
        cin >> nrFinalStates;
        cout << "Introduceti starile finale: ";
        for (int i = 0; i < nrFinalStates; ++i) {
            cin >> state;
            finalStates.insert(state);
        }
    }

    void AutomatFinit::citireFisier(const string& filename) {
        ifstream file(filename);

        if (!file.is_open()) {
            cerr << "Eroare la deschiderea fisierului." << endl;
            return;
        }

        int nrStates, nrFinalStates, nrTransitions;
        string state;

        file >> nrStates;
        for (int i = 0; i < nrStates; ++i) {
            file >> state;
            states.insert(state);
        }

        int alphabetSize;
        file >> alphabetSize;
        for (int i = 0; i < alphabetSize; ++i) {
            char symbol;
            file >> symbol;
            alphabet.insert(symbol);
        }

        file >> nrTransitions;
        for (int i = 0; i < nrTransitions; ++i) {
            string currentState, nextState;
            char symbol;
            file >> currentState >> symbol >> nextState;

            if (transitions.contains( {currentState, symbol} )) {
                cerr << "\n Error: Automatul nu este determinist!\n" << "Tranzitie nedeterminista detectata la starea " << currentState << " cu simbolul " << symbol << "." << endl;
                file.close();
                return;
            }
            transitions[{currentState, symbol}] = nextState;
        }

        file >> startState;

        file >> nrFinalStates;
        for (int i = 0; i < nrFinalStates; ++i) {
            file >> state;
            finalStates.insert(state);
        }

        file.close();
    }

    void AutomatFinit::afisareStari() const
    {
        cout << "Starile sunt: ";
        for (const auto& state : states) {
            cout << state << " ";
        }
        cout << endl;
    }

    void AutomatFinit::afisareAlfabet() const
    {
        cout << "Alfabetul este: ";
        for (const auto& symbol : alphabet) {
            cout << symbol << " ";
        }
        cout << endl;
    }

    void AutomatFinit::afisareTranzitii() const
    {
        cout << "Tranzitiile sunt: " << endl;
        for (const auto& transition : transitions) {
            cout << "(" << transition.first.first << ", " << transition.first.second << ") -> " << transition.second << endl;
        }
    }

    void AutomatFinit::afisareStariFinale() const
    {
        cout << "Starile finale sunt: ";
        for (const auto& state : finalStates) {
            cout << state << " ";
        }
        cout << endl;
    }

    bool AutomatFinit::verificaSecventa(const string& secventa) {
        string currentState = startState;

        for (const auto& symbol : secventa) {
            if (!transitions.contains({currentState, symbol})) {
                return false;
            }
            currentState = transitions[{currentState, symbol}];
        }

        return finalStates.contains(currentState);
    }

    string AutomatFinit::celMaiLungPrefixAcceptat(const string& secventa) {
        string currentState = startState;
        string longestPrefix;
        string currentPrefix;

        for (const auto& symbol : secventa) {
            if (!transitions.contains({currentState, symbol})) {
                break;
            }
            currentState = transitions[{currentState, symbol}];
            currentPrefix += symbol;
            if (finalStates.contains(currentState)) {
                longestPrefix = currentPrefix;
            }
        }

        return longestPrefix;
    }


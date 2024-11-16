//
// Created by Emi on 5/17/2024.
//

#include <iostream>
#include "../lab5/ciclu_eulerian.h"
#include "../lab5/ford_fulkerson.h"
#include "../lab5/pompare_topologica.h"

using namespace std;

int main() {
    int optiune;

    while (true) {
        cout << "1. CICLU EULERIAN\n2. FORD FULKERSON\n3. POMPARE TOPOLOGICA\n";
        cout << "Introduceti optiunea: ";
        cin >> optiune;

        if (optiune == 1) {
            ce();
        } else if (optiune == 2) {
            ff();
        } else if (optiune == 3) {
            pt();
        } else {
            break;
        }
        cout << endl;
    }
}
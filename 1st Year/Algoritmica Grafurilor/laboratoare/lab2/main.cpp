#include "ex1.h"
#include "ex2.h"
#include "ex3.h"
#include "ex4.h"
#include "ex5.h"

int main() {

    while (1) {
        cout << "1.Problema 1\n2.Problema 2\n3.Problema 3\n4.Problema 4\n5.Problema 5\n0.Iesire\n";
        int optiune;
        cin >> optiune;
        if (optiune == 1) {
            run_1();
        } else if (optiune == 0) {
            cout << "pa";
            return 0;
        } else if (optiune == 2) {
            run_2();
        }else if (optiune == 3) {
            run_3();
        } else if (optiune == 4) {
            run_4();
        } else if (optiune == 5) {
            run_5();
        }
        else {
            cout << "Optiune invalida\n";
        }
        cout << "\n";
    }
}

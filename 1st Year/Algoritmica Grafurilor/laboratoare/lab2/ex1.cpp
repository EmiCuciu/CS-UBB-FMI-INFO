/*
 * Implementați algoritmul lui Moore pentru un graf orientat neponderat (algoritm bazat pe Breath-first search, vezi cursul 2).
 * Datele sunt citete din fisierul graf.txt. Primul rând din graf.txt conține numărul vârfurilor, iar următoarele rânduri
 * conțin muchiile grafului. Programul trebuie să afiseze lanțul cel mai scurt dintr-un vârf (vârful sursa poate fi citit de la tastatura).

    Graf orientat neponderat = graf in care muchiile au directie si nu au costuri
 */

#include <iostream>
#include <fstream>
#include <vector>
#include <queue>

using namespace std;

void citesteGraf(const string &filename, vector<vector<int>> &graf) {
    ifstream in(filename);
    if (!in.is_open()) {
        throw std::runtime_error("Nu s-a putut deschide fisierul");
    }

    int nrVarfuri;
    in >> nrVarfuri;

    graf.resize(nrVarfuri); /// vectorul meu graf se redimensioneaza dupa nr de varfuri

    int varf1, varf2;

    while (in >> varf1 >> varf2) {
        graf[varf1].push_back(varf2);
    }
    in.close();
}

void Moore(const vector<vector<int>> &graf, int start) {
    int nrVarfuri = graf.size();
    vector<bool> visited(nrVarfuri, false);
    vector<int> distanta(nrVarfuri, INT_MAX);   ///cu o val mare, infinit
    queue<int> q;

    visited[start] = true;
    distanta[start] = 0;
    q.push(start);

    while (!q.empty()) {
        int varf_curent = q.front();    /// extrage primul element din coada
        q.pop();

        for (int vecin: graf[varf_curent]) {        ///range-based for loop în C++, am facut la seminar la oop, echivalent cu for(int i=0;i<graf[varf_curent].size;++i)
            if (!visited[vecin]) {         /// daca nu a fost vizitat
                visited[vecin] = true;
                distanta[vecin] = distanta[varf_curent] + 1;
                q.push(vecin);
            }
        }
    }

    cout << "Cele mai scurte drumuri de la varful " << start << ":\n";
    for (int i = 0; i < nrVarfuri; ++i) {
        cout << "Varful" << i << ": ";
        if (distanta[i] == INT_MAX) {
            cout << "Nu ajunge la acel varf\n";
        } else {
            cout << distanta[i] << " pasi\n";
        }
    }
}

void afisare(const string &filename){
    ifstream file(filename);

    string line;
    while (getline(file,line)){
        cout<<line<<endl;
    }
    file.close();
}

void run_1() {
    string filename = "W:\\Facultate S2\\Algoritmica grafurilor\\teme\\lab2\\graf.txt";
    vector<vector<int>> graf;
    citesteGraf(filename, graf);
    afisare(filename);
    int varf_start;
    cout << "Introdu varful de start: ";
    cin >> varf_start;

    Moore(graf,varf_start);

}
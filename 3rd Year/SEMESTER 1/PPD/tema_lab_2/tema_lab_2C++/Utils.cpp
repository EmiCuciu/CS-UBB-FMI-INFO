#include "Utils.h"

#include <fstream>
#include <iostream>
#include <random>

using namespace std;

void Utils::readData(const string &filePath, int &N, int &M, int &n, int &p, vector<vector<int> > &matrix,
                     vector<vector<int> > &convMatrix) {
    ifstream fin(filePath);
    if (!fin.is_open()) {
        cerr << "Nu s-a putut deschide fisierul: " << filePath << endl;
        exit(1);
    }

    fin >> N >> M >> n >> p;

    matrix.assign(N, vector<int>(M));
    convMatrix.assign(n, vector<int>(n));

    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            fin >> matrix[i][j];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            fin >> convMatrix[i][j];

    fin.close();
}


bool Utils::compareFiles(const string &file1, const string &file2) {
    ifstream f1(file1), f2(file2);
    if (!f1.is_open() || !f2.is_open()) {
        cerr << "Eroare la deschiderea fisierelor \n";
        return false;
    }

    string line1, line2;

    vector<string> lines1, lines2;

    while (getline(f1, line1)) {
        line1.erase(line1.find_last_not_of(" \t\r\n") + 1);
        if (!line1.empty()) lines1.push_back(line1);
    }

    while (getline(f2, line2)) {
        line2.erase(line2.find_last_not_of(" \t\r\n") + 1);
        if (!line2.empty()) lines2.push_back(line2);
    }

    if (lines1.size() != lines2.size()) {
        cerr << "Fisierele au numere diferite de linii substantiale\n";
        return false;
    }

    for (size_t i = 0; i < lines1.size(); i++) {
        if (lines1[i] != lines2[i]) {
            cerr << "Diferenta la linia " << (i + 1) << "\n";
            return false;
        }
    }

    return true;
}

void Utils::generateData(const string &filePath, int N, int M, int n, int p) {
    ofstream fout(filePath);
    if (!fout.is_open()) {
        cerr << "Nu s-a putut crea fisierul: " << filePath << endl;
        exit(1);
    }

    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> matrixDist(0, 255);
    uniform_int_distribution<> convDist(0, 1);

    fout << N << " " << M << " " << n << " " << p << "\n";

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            fout << matrixDist(gen);
            if (j < M - 1) fout << " ";
        }
        fout << "\n";
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            fout << convDist(gen);
            if (j < n - 1) fout << " ";
        }
        fout << "\n";
    }

    fout.close();
    cout << "Fisierul " << filePath << " a fost generat cu succes.\n";
}

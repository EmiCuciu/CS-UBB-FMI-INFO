#include <chrono>
#include <iostream>
#include <vector>

#include "SecventialDinamic.h"
#include "SecventialStatic.h"
#include "Utils.h"

using namespace std;

int main()
{
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;

    Utils::readData("../data/date.txt", N, M,
                    n, p, matrix, convMatrix);

    int staticMatrix[10][10]{}, staticConv[5][5]{};
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            staticMatrix[i][j] = matrix[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            staticConv[i][j] = convMatrix[i][j];

    SecventialStatic staticSolver(N, M, n, staticMatrix, staticConv);
    auto start = chrono::high_resolution_clock::now();
    auto& staticResult = staticSolver.run();
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> duration = end - start;
    cout << "Timpul de executie pentru Secvential Static: " << duration.count() << " ms\n";


    SecventialDinamic dynamicSolver(N, M, n);
    start = chrono::high_resolution_clock::now();
    SecventialDinamic::run(matrix, convMatrix);
    end = chrono::high_resolution_clock::now();
    duration = end - start;
    cout << "Timpul de executie pentru Secvential Dinamic: " << duration.count() << " ms\n";


    bool areEqual = Utils::compareFiles("../data/outputSecventialStatic.txt", "../data/outputSecventialDinamic.txt");
    if (areEqual)
        cout << "Identice.\n";
    else
        cout << "Diferite.\n";

    return 0;
}

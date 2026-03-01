#include <iostream>
#include <vector>
#include <chrono>
#include "Utils.h"
#include "SecventialStatic.h"
#include "SecventialDinamic.h"
#include "LiniiStatic.h"
#include "LiniiDinamic.h"
#include "ColoaneStatic.h"
#include "ColoaneDinamic.h"

double try_secvential_static(int N, int M, int n, const vector<vector<int>>& matrix,
                             const vector<vector<int>>& convMatrix)
{
    int staticMatrix[1000][10]{}, staticConv[5][5]{};

    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            staticMatrix[i][j] = matrix[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            staticConv[i][j] = convMatrix[i][j];

    SecventialStatic solver(N, M, n, staticMatrix, staticConv);

    auto start = chrono::high_resolution_clock::now();
    solver.run();
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

double try_secvential_dinamic(const vector<vector<int>>& matrix, const vector<vector<int>>& convMatrix)
{
    auto start = chrono::high_resolution_clock::now();
    SecventialDinamic::run(matrix, convMatrix);
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

double try_linii_static(int N, int M, int n, int p, const vector<vector<int>>& matrix,
                        const vector<vector<int>>& convMatrix)
{
    int staticMatrix[1000][10]{}, staticConv[5][5]{};

    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            staticMatrix[i][j] = matrix[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            staticConv[i][j] = convMatrix[i][j];

    LiniiStatic solver(N, M, n, p, staticMatrix, staticConv);

    auto start = chrono::high_resolution_clock::now();
    solver.run();
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

double try_linii_dinamic(int N, int M, int n, int p, const vector<vector<int>>& matrix,
                         const vector<vector<int>>& convMatrix)
{
    LiniiDinamic solver(N, M, n, p);
    solver.loadData(matrix, convMatrix);

    auto start = chrono::high_resolution_clock::now();
    solver.run();
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

double try_coloane_static(int N, int M, int n, int p, const vector<vector<int>>& matrix,
                          const vector<vector<int>>& convMatrix)
{
    int staticMatrix[1000][10]{}, staticConv[5][5]{};

    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            staticMatrix[i][j] = matrix[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            staticConv[i][j] = convMatrix[i][j];

    ColoaneStatic solver(N, M, n, p, staticMatrix, staticConv);

    auto start = chrono::high_resolution_clock::now();
    solver.run();
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

double try_coloane_dinamic(int N, int M, int n, int p, const vector<vector<int>>& matrix,
                           const vector<vector<int>>& convMatrix)
{
    ColoaneDinamic solver(N, M, n, p);
    solver.loadData(matrix, convMatrix);

    auto start = chrono::high_resolution_clock::now();
    solver.run();
    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> duration = end - start;
    return duration.count();
}

void secvential()
{
    cout << "Secvential:\n";
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;
    Utils::readData(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/date.txt", N, M,
        n, p, matrix, convMatrix);

    double t_static = try_secvential_static(N, M, n, matrix, convMatrix);
    double t_dynamic = try_secvential_dinamic(matrix, convMatrix);
    Utils::compareFiles(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputSecventialStatic.txt",
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputSecventialDinamic.txt");
    cout << "Timp static: " << t_static << " ms | Timp dinamic: " << t_dynamic << " ms\n";
}

void linii()
{
    cout << "Linii:\n";
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;
    Utils::readData(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/date.txt", N, M,
        n, p, matrix, convMatrix);

    double t_linii_static = try_linii_static(N, M, n, p, matrix, convMatrix);
    double t_linii_dinamic = try_linii_dinamic(N, M, n, p, matrix, convMatrix);
    Utils::compareFiles(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputLiniiStatic.txt",
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputLiniiDinamic.txt");
    cout << "Timp static: " << t_linii_static << " ms | Timp dinamic: " << t_linii_dinamic << " ms\n";
}

void coloane()
{
    cout << "Coloane:\n";
    int N, M, n, p;
    vector<vector<int>> matrix, convMatrix;
    Utils::readData(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/date.txt", N, M,
        n, p, matrix, convMatrix);


    double t_coloane_static = try_coloane_static(N, M, n, p, matrix, convMatrix);
    double t_coloane_dinamic = try_coloane_dinamic(N, M, n, p, matrix, convMatrix);
    Utils::compareFiles(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputColoaneStatic.txt",
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputColoaneDinamic.txt");
    cout << "Timp static: " << t_coloane_static << " ms | Timp dinamic: " << t_coloane_dinamic << " ms\n";
}

int main(int argc, char* argv[])
{
    cout << "Pornit\n";
    const std::string dataPath =
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/date.txt";

    int N, M, n, p;
    std::vector<std::vector<int>> matrix, convMatrix;
    Utils::readData(dataPath, N, M, n, p, matrix, convMatrix);

    secvential();
    linii();
    coloane();

    // double t_secvential_static = try_secvential_static(N, M, n, matrix, convMatrix);
    // cout << t_secvential_static;


    return 0;
}

//
// Created by emicu on 10/12/2025.
//
#include <fstream>

#include "SecventialDinamic.h"
SecventialDinamic::SecventialDinamic(int N, int M, int n) : N(N), M(M), n(n) { allocateMatrices(); }
SecventialDinamic::~SecventialDinamic() { deallocateMatrices(); }

void SecventialDinamic::allocateMatrices()
{
    matrix = new int*[N];
    for (int i = 0; i < N; i++) matrix[i] = new int[M];
    convMatrix = new int*[n];
    for (int i = 0; i < n; i++) convMatrix[i] = new int[n];
    resultMatrix = new int*[N];
    for (int i = 0; i < N; i++) resultMatrix[i] = new int[M];
}

void SecventialDinamic::deallocateMatrices() const
{
    for (int i = 0; i < N; i++) delete[] matrix[i];
    delete[] matrix;
    for (int i = 0; i < n; i++) delete[] convMatrix[i];
    delete[] convMatrix;
    for (int i = 0; i < N; i++) delete[] resultMatrix[i];
    delete[] resultMatrix;
}

void SecventialDinamic::run(const std::vector<std::vector<int>>& srcMatrix,
                            const std::vector<std::vector<int>>& srcConv)
{
    int offset = srcConv.size() / 2;

    int N = srcMatrix.size();
    int M = srcMatrix[0].size();
    int n = srcConv.size();

    SecventialDinamic secventialDinamic(N, M, n);

    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            secventialDinamic.matrix[i][j] = srcMatrix[i][j];

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            secventialDinamic.convMatrix[i][j] = srcConv[i][j];

    for (int row = 0; row < N; row++)
        for (int col = 0; col < M; col++)
        {
            int sum = 0;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                {
                    int ii = row - offset + i;
                    int jj = col - offset + j;
                    if (ii < 0) ii = 0;
                    if (ii >= N) ii = N - 1;
                    if (jj < 0) jj = 0;
                    if (jj >= M) jj = M - 1;

                    sum += secventialDinamic.matrix[ii][jj] * secventialDinamic.convMatrix[i][j];
                }
            secventialDinamic.resultMatrix[row][col] = sum;
        }

    writeToFile(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputSecventialDinamic.txt",
        secventialDinamic.resultMatrix, N, M);
}

void SecventialDinamic::writeToFile(const char* path, int** arr, int N, int M)
{
    std::ofstream fout(path);
    for (int i = 0; i < N; ++i)
    {
        for (int j = 0; j < M; ++j)
        {
            fout << arr[i][j];
            if (j < M - 1) fout << " ";
        }
        fout << "\n";
    }

    fout.close();
}

//
// Created by emicu on 10/12/2025.
//
#include <fstream>

#include "SecventialStatic.h"

SecventialStatic::SecventialStatic(int N, int M, int n, const int srcMatrix[1000][10], const int srcConv[5][5])
    : N(N), M(M), n(n)
{
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            matrix[i][j] = srcMatrix[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            convMatrix[i][j] = srcConv[i][j];
}

int (& SecventialStatic::run())[1000][10]
{
    int offset = n / 2;

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

                    sum += matrix[ii][jj] * convMatrix[i][j];
                }
            resultMatrix[row][col] = sum;
        }

    writeToFile(
        "D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputSecventialStatic.txt",
        resultMatrix, N, M);

    return resultMatrix;
}

void SecventialStatic::writeToFile(const char* path, int arr[1000][10], int N, int M)
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
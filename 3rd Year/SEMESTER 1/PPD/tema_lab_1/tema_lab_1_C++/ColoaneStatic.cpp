//
// Created by emicu on 10/12/2025.
//

#include "ColoaneStatic.h"
#include <fstream>

ColoaneStatic::ColoaneStatic(int N, int M, int n, int p,
                             const int srcMatrix[1000][10], const int srcConv[5][5])
    : N(N), M(M), n(n), p(p) {
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            matrix[i][j] = srcMatrix[i][j];

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            convMatrix[i][j] = srcConv[i][j];
}

void ColoaneStatic::worker(int startCol, int endCol) {
    int offset = n / 2;

    for (int col = startCol; col < endCol; ++col)
        for (int row = 0; row < N; ++row) {
            int sum = 0;
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n; ++j) {
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
}

void ColoaneStatic::run() {
    std::vector<std::thread> threads;
    int colsPerThread = M / p;
    int extra = M % p;
    int start = 0;

    for (int t = 0; t < p; ++t) {
        int end = start + colsPerThread + (extra-- > 0 ? 1 : 0);
        threads.emplace_back(&ColoaneStatic::worker, this, start, end);
        start = end;
    }

    for (auto& th : threads)
        th.join();

    writeToFile("D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputColoaneStatic.txt", resultMatrix);
}

void ColoaneStatic::writeToFile(const char* path, int arr[1000][10]) {
    std::ofstream fout(path);
    for (int i = 0; i < 1000; ++i) {
        for (int j = 0; j < 10; ++j)
            fout << arr[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

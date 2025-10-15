//
// Created by emicu on 10/12/2025.
//

#include "ColoaneDinamic.h"
#include <fstream>

ColoaneDinamic::ColoaneDinamic(int N, int M, int n, int p)
    : N(N), M(M), n(n), p(p) {
    allocate();
}

ColoaneDinamic::~ColoaneDinamic() { deallocate(); }

void ColoaneDinamic::allocate() {
    matrix = new int*[N];
    resultMatrix = new int*[N];
    for (int i = 0; i < N; ++i) {
        matrix[i] = new int[M];
        resultMatrix[i] = new int[M];
    }

    convMatrix = new int*[n];
    for (int i = 0; i < n; ++i)
        convMatrix[i] = new int[n];
}

void ColoaneDinamic::deallocate() {
    for (int i = 0; i < N; ++i) {
        delete[] matrix[i];
        delete[] resultMatrix[i];
    }
    delete[] matrix;
    delete[] resultMatrix;

    for (int i = 0; i < n; ++i)
        delete[] convMatrix[i];
    delete[] convMatrix;
}

void ColoaneDinamic::loadData(const std::vector<std::vector<int>>& mat, const std::vector<std::vector<int>>& conv) {
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            matrix[i][j] = mat[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            convMatrix[i][j] = conv[i][j];
}

void ColoaneDinamic::worker(int startCol, int endCol) {
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

void ColoaneDinamic::run() {
    std::vector<std::thread> threads;
    int colsPerThread = M / p;
    int extra = M % p;
    int start = 0;

    for (int t = 0; t < p; ++t) {
        int end = start + colsPerThread + (extra-- > 0 ? 1 : 0);
        threads.emplace_back(&ColoaneDinamic::worker, this, start, end);
        start = end;
    }

    for (auto& th : threads)
        th.join();

    writeToFile("D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/data/outputColoaneDinamic.txt", resultMatrix, N, M);
}

void ColoaneDinamic::writeToFile(const char* path, int** arr, int N, int M) {
    std::ofstream fout(path);
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            fout << arr[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

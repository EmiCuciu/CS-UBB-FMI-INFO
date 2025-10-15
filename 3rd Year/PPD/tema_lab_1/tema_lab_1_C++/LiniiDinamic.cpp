//
// Created by emicu on 10/12/2025.
//

#include "LiniiDinamic.h"
#include <fstream>

LiniiDinamic::LiniiDinamic(int N, int M, int n, int p)
    : N(N), M(M), n(n), p(p) {
    allocate();
}

LiniiDinamic::~LiniiDinamic() { deallocate(); }

void LiniiDinamic::allocate() {
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

void LiniiDinamic::deallocate() {
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

void LiniiDinamic::loadData(const std::vector<std::vector<int>>& mat, const std::vector<std::vector<int>>& conv) {
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            matrix[i][j] = mat[i][j];
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            convMatrix[i][j] = conv[i][j];
}

void LiniiDinamic::worker(int startRow, int endRow) {
    int offset = n / 2;
    for (int row = startRow; row < endRow; ++row)
        for (int col = 0; col < M; ++col) {
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

void LiniiDinamic::run() {
    std::vector<std::thread> threads;
    int rowsPerThread = N / p;
    int extra = N % p;
    int start = 0;

    for (int t = 0; t < p; ++t) {
        int end = start + rowsPerThread + (extra-- > 0 ? 1 : 0);
        threads.emplace_back(&LiniiDinamic::worker, this, start, end);
        start = end;
    }

    for (auto& th : threads)
        th.join();

    writeToFile("D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/data/outputLiniiDinamic.txt", resultMatrix, N, M);
}

void LiniiDinamic::writeToFile(const char* path, int** arr, int N, int M) {
    std::ofstream fout(path);
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            fout << arr[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

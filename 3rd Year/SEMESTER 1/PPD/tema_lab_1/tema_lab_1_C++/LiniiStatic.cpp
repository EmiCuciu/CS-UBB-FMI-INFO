#include "LiniiStatic.h"
#include <fstream>

LiniiStatic::LiniiStatic(int N, int M, int n, int p,
                         const int srcMatrix[1000][10], const int srcConv[5][5])
    : N(N), M(M), n(n), p(p) {
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            matrix[i][j] = srcMatrix[i][j];

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            convMatrix[i][j] = srcConv[i][j];
}

void LiniiStatic::worker(int startRow, int endRow) {
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

void LiniiStatic::run() {
    std::vector<std::thread> threads;
    int rowsPerThread = N / p;
    int extra = N % p;
    int start = 0;

    for (int t = 0; t < p; ++t) {
        int end = start + rowsPerThread + (extra-- > 0 ? 1 : 0);
        threads.emplace_back(&LiniiStatic::worker, this, start, end);
        start = end;
    }

    for (auto& th : threads)
        th.join();

    writeToFile("D:/GithubRepositories/CS-UBB-FMI-INFO/3rd Year/PPD/tema_lab_1/tema_lab_1_C++/cmake-build-release/outputLiniiStatic.txt", resultMatrix, N, M);
}

void LiniiStatic::writeToFile(const char* path, int arr[1000][10], int N, int M) {
    std::ofstream fout(path);
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            fout << arr[i][j] << (j < M-1 ? " " : "");
        fout << "\n";
    }
    fout.close();
}

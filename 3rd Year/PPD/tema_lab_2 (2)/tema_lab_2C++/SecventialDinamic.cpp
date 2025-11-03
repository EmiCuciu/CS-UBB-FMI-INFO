#include "SecventialDinamic.h"
#include <fstream>
#include <vector>

SecventialDinamic::SecventialDinamic(const int N, const int M, const int n) : N(N), M(M), n(n) {
    allocateMatrices();
}

SecventialDinamic::~SecventialDinamic() {
    deallocateMatrices();
}

void SecventialDinamic::allocateMatrices() {
    matrix = new int *[N];
    for (int i = 0; i < N; i++)
        matrix[i] = new int[M];

    convMatrix = new int *[n];
    for (int i = 0; i < n; i++)
        convMatrix[i] = new int[n];
}

void SecventialDinamic::deallocateMatrices() const {
    for (int i = 0; i < N; i++)
        delete[] matrix[i];
    delete[] matrix;

    for (int i = 0; i < n; i++)
        delete[] convMatrix[i];
    delete[] convMatrix;
}

void SecventialDinamic::loadData(const std::vector<std::vector<int> > &srcMatrix,
                                 const std::vector<std::vector<int> > &srcConv) const {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            matrix[i][j] = srcMatrix[i][j];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            convMatrix[i][j] = srcConv[i][j];
}

void SecventialDinamic::run() const {
    const int offset = n / 2;
    std::vector<int> prevRow(M);

    // save linie precedenta
    for (int j = 0; j < M; j++)
        prevRow[j] = matrix[0][j];

    for (int row = 0; row < N; row++) {
        std::vector<int> currentRow(M);

        // save linia curenta inainte de modif
        for (int col = 0; col < M; col++)
            currentRow[col] = matrix[row][col];

        for (int col = 0; col < M; col++) {
            int sum = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int ii = row - offset + i;
                    int jj = col - offset + j;

                    if (ii < 0) ii = 0;
                    if (ii >= N) ii = N - 1;
                    if (jj < 0) jj = 0;
                    if (jj >= M) jj = M - 1;

                    int val;
                    if (ii < row)
                        val = prevRow[jj]; // vecinii de sus (modificați)
                    else if (ii == row)
                        val = currentRow[jj];
                    else
                        val = matrix[ii][jj];

                    sum += val * convMatrix[i][j];
                }
            }

            matrix[row][col] = sum; // modificam direct matrix
        }

        prevRow = currentRow; // pregatim urm iteratie
    }
}

void SecventialDinamic::writeToFile(const char *path) const {
    std::ofstream fout(path);
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++)
            fout << matrix[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

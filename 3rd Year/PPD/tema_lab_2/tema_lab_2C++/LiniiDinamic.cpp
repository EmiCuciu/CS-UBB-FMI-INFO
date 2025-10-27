#include "LiniiDinamic.h"
#include <fstream>

LiniiDinamic::LiniiDinamic(const int N, const int M, const int n, const int p)
    : N(N), M(M), n(n), p(p) {
    allocate();
    // Alocăm spațiul pentru frontiere.
    // Fiecare thread 't' salvează linia 'start' la indexul 2*t
    // și linia 'end-1' la indexul 2*t + 1
    savedBoundaries.resize(2 * p, vector<int>(M));
}

LiniiDinamic::~LiniiDinamic() {
    deallocate();
}

void LiniiDinamic::allocate() {
    matrix = new int *[N];
    for (int i = 0; i < N; i++)
        matrix[i] = new int[M];

    convMatrix = new int *[n];
    for (int i = 0; i < n; i++)
        convMatrix[i] = new int[n];
}

void LiniiDinamic::deallocate() const {
    for (int i = 0; i < N; i++)
        delete[] matrix[i];
    delete[] matrix;

    for (int i = 0; i < n; i++)
        delete[] convMatrix[i];
    delete[] convMatrix;
}

void LiniiDinamic::loadData(const vector<vector<int>> &mat,
                            const vector<vector<int>> &conv) const {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            matrix[i][j] = mat[i][j];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            convMatrix[i][j] = conv[i][j];
}

void LiniiDinamic::calculateRow(const vector<int>& prev,
                                const vector<int>& current,
                                const vector<int>& next,
                                vector<int>& result) const {
    int offset = n / 2;

    for (int col = 0; col < M; col++) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int jj = col - offset + j;
                if (jj < 0) jj = 0;
                if (jj >= M) jj = M - 1;

                int val;
                if (i < offset) {
                    val = prev[jj];
                } else if (i > offset) {
                    val = next[jj];
                } else {
                    val = current[jj];
                }
                sum += val * convMatrix[i][j];
            }
        }
        result[col] = sum;
    }
}

void LiniiDinamic::worker(int t, int startRow, int endRow, my_barrier &barrier) {
    // PASUL 1: Copierea frontierelor proprii în vectorii auxiliari partajați
    for (int j = 0; j < M; j++) {
        savedBoundaries[2 * t][j] = matrix[startRow][j];
        savedBoundaries[2 * t + 1][j] = matrix[endRow - 1][j];
    }

    // PASUL 2: Barieră de sincronizare
    barrier.wait();

    // PASUL 3: Actualizarea elementelor folosind vectorii auxiliari
    // Folosim buffere locale O(M) pentru a rula algoritmul secvențial
    // in-place pe bucata [startRow, endRow)
    vector<int> prevRow(M);
    vector<int> currentRow(M);
    vector<int> nextRow(M);
    vector<int> newRow(M);

    // Initializam prevRow cu frontiera de sus (de la thread-ul t-1)
    if (t == 0) {
        // Primul thread, border handling
        prevRow = savedBoundaries[2 * t]; // Propria linie de start
    } else {
        // Citim linia 'end-1' a thread-ului t-1
        prevRow = savedBoundaries[2 * (t - 1) + 1];
    }

    // Initializam currentRow cu prima linie proprie (pe care am salvat-o)
    currentRow = savedBoundaries[2 * t];

    for (int row = startRow; row < endRow; row++) {
        // Citim linia urmatoare
        if (row == endRow - 1) {
            // Suntem la ultima linie, citim frontiera de jos (de la t+1)
            if (t == p - 1) {
                // Ultimul thread, border handling
                nextRow = savedBoundaries[2 * t + 1]; // Propria linie de final
            } else {
                // Citim linia 'start' a thread-ului t+1
                nextRow = savedBoundaries[2 * (t + 1)];
            }
        } else if (row == endRow - 2) {
            // Suntem la penultima linie, citim ultima (pe care am salvat-o)
            nextRow = savedBoundaries[2 * t + 1];
        } else {
            // Linie internă, citim direct din matricea originală
            for (int j = 0; j < M; j++) {
                nextRow[j] = matrix[row + 1][j];
            }
        }

        // Calculam noul rand folosind prev, current, next (care sunt VALORI ORIGINALE)
        calculateRow(prevRow, currentRow, nextRow, newRow);

        // Suprascriem linia in matricea originala [cite: 4]
        for (int j = 0; j < M; j++)
            matrix[row][j] = newRow[j];

        // Shiftam bufferele
        prevRow = currentRow;
        currentRow = nextRow;
    }
}

void LiniiDinamic::run() {
    vector<thread> threads;
    my_barrier barrier(p);

    int rowsPerThread = N / p;
    int extra = N % p;
    int start = 0;

    for (int t = 0; t < p; ++t) {
        int end = start + rowsPerThread + (extra > 0 ? 1 : 0);
        if (extra > 0) extra--;

        threads.emplace_back(&LiniiDinamic::worker, this, t, start, end,
                            ref(barrier));
        start = end;
    }

    for (auto &th : threads)
        th.join();
}

void LiniiDinamic::writeToFile(const char *path) const {
    ofstream fout(path);
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++)
            fout << matrix[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}
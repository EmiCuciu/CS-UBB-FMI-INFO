#include "LiniiDinamic.h"
#include <fstream>

LiniiDinamic::LiniiDinamic(const int N, const int M, const int n, const int p)
    : N(N), M(M), n(n), p(p) {
    allocate();
}

LiniiDinamic::~LiniiDinamic() {
    deallocate();
}

void LiniiDinamic::allocate() {
    matrix = new int *[N];
    for (int i = 0; i < N; i++) {
        matrix[i] = new int[M];
    }

    convMatrix = new int *[n];
    for (int i = 0; i < n; i++)
        convMatrix[i] = new int[n];
}

void LiniiDinamic::deallocate() const {
    for (int i = 0; i < N; i++) {
        delete[] matrix[i];
    }
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

void LiniiDinamic::worker(int threadId, int totalThreads, my_barrier &barrierStart, my_barrier &barrierEnd,
                          vector<int> &sharedPrev, vector<int> &sharedCur,
                          vector<int> &sharedTemp) const {
    int offset = n / 2;

    // Calculate column range for this thread once
    int colsPerThread = M / totalThreads;
    int extra = M % totalThreads;
    int startCol = threadId * colsPerThread + min(threadId, extra);
    int endCol = startCol + colsPerThread + (threadId < extra ? 1 : 0);

    for (int row = 0; row < N; row++) {
        // Thread 0 prepares sharedCur
        if (threadId == 0) {
            for (int j = 0; j < M; j++) {
                sharedCur[j] = matrix[row][j];
            }
        }

        // Wait for sharedCur to be ready
        barrierStart.wait();

        // Compute convolution for assigned columns
        for (int col = startCol; col < endCol; col++) {
            long long sum = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int ii = row - offset + i;
                    int jj = col - offset + j;

                    // Clipping
                    if (ii < 0) ii = 0;
                    if (ii >= N) ii = N - 1;
                    if (jj < 0) jj = 0;
                    if (jj >= M) jj = M - 1;

                    int val;
                    if (ii < row) {
                        val = sharedPrev[jj];
                    } else if (ii == row) {
                        val = sharedCur[jj];
                    } else {
                        val = matrix[ii][jj];
                    }
                    sum += (long long)val * (long long)convMatrix[i][j];
                }
            }

            sharedTemp[col] = (int)sum;
        }

        // Wait for all threads to finish computing
        barrierEnd.wait();

        // Copy results back to matrix
        for (int col = startCol; col < endCol; col++) {
            matrix[row][col] = sharedTemp[col];
        }

        // Thread 0 updates sharedPrev for next iteration
        if (threadId == 0) {
            for (int j = 0; j < M; j++)
                sharedPrev[j] = sharedCur[j];
        }

        // Ensure sharedPrev is updated before next iteration
        barrierStart.wait();
    }
}

void LiniiDinamic::run() {
    vector<thread> threads;
    my_barrier barrierStart(p);
    my_barrier barrierEnd(p);

    vector<int> sharedPrev(M);
    vector<int> sharedCur(M);
    vector<int> sharedTemp(M);

    // Initialize sharedPrev = matrix[0]
    for (int j = 0; j < M; j++)
        sharedPrev[j] = matrix[0][j];

    for (int t = 0; t < p; t++) {
        threads.emplace_back(&LiniiDinamic::worker, this, t, p,
                             ref(barrierStart), ref(barrierEnd),
                             ref(sharedPrev), ref(sharedCur), ref(sharedTemp));
    }

    for (auto &th: threads)
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

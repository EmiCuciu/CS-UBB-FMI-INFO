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

void LiniiDinamic::loadData(const vector<vector<int> > &mat,
                            const vector<vector<int> > &conv) const {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            matrix[i][j] = mat[i][j];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            convMatrix[i][j] = conv[i][j];
}

void LiniiDinamic::worker(int threadId, int totalThreads, my_barrier &barrierCompute, my_barrier &barrierCopy,
                          my_barrier &barrierUpdate, vector<int> &sharedPrev, vector<int> &sharedCur,
                          vector<int> &sharedTemp) const {
    int offset = n / 2;

    for (int row = 0; row < N; row++) {
        // Thread 0 prepares sharedCur (copy of original matrix[row])
        if (threadId == 0) {
            for (int j = 0; j < M; j++) {
                sharedCur[j] = matrix[row][j];
            }
        }
        // Ensure all threads see sharedCur
        barrierCompute.wait();

        // Compute portion of columns for this thread
        int colsPerThread = M / totalThreads;
        int extra = M % totalThreads;
        int startCol = threadId * colsPerThread + min(threadId, extra);
        int endCol = startCol + colsPerThread + (threadId < extra ? 1 : 0);

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
                        // Use sharedPrev (original previous row)
                        val = sharedPrev[jj];
                    } else if (ii == row) {
                        // Use sharedCur (original current row)
                        val = sharedCur[jj];
                    } else {
                        // Rows below are still original in matrix
                        val = matrix[ii][jj];
                    }
                    sum += (long long)val * (long long)convMatrix[i][j];
                }
            }

            sharedTemp[col] = (int)sum;
        }

        // Signal compute done for this row
        barrierCopy.wait();

        // Copy results from sharedTemp to matrix (each thread its portion)
        for (int col = startCol; col < endCol; col++) {
            matrix[row][col] = sharedTemp[col];
        }

        // Ensure all copied before updating sharedPrev
        barrierUpdate.wait();

        // Thread 0 updates sharedPrev = sharedCur (for next row)
        if (threadId == 0) {
            for (int j = 0; j < M; j++)
                sharedPrev[j] = sharedCur[j];
        }

        // Ensure update propagated before next iteration
        barrierCompute.wait();
    }
}

void LiniiDinamic::run() {
    vector<thread> threads;
    my_barrier barrierCompute(p);
    my_barrier barrierCopy(p);
    my_barrier barrierUpdate(p);

    vector<int> sharedPrev(M);
    vector<int> sharedCur(M);
    vector<int> sharedTemp(M);

    // Initialize sharedPrev = matrix[0] (original)
    for (int j = 0; j < M; j++)
        sharedPrev[j] = matrix[0][j];

    for (int t = 0; t < p; t++) {
        threads.emplace_back(&LiniiDinamic::worker, this, t, p,
                             ref(barrierCompute), ref(barrierCopy), ref(barrierUpdate),
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

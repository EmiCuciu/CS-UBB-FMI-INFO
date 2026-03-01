#include "LiniiDinamic.h"
#include <fstream>

LiniiDinamic::LiniiDinamic(const int N, const int M, const int n, const int p)
    : N(N), M(M), n(n), p(p)
{
    allocate();
}

LiniiDinamic::~LiniiDinamic()
{
    deallocate();
}

void LiniiDinamic::allocate()
{
    matrix = new int*[N];
    for (int i = 0; i < N; i++)
        matrix[i] = new int[M];

    convMatrix = new int*[n];
    for (int i = 0; i < n; i++)
        convMatrix[i] = new int[n];
}

void LiniiDinamic::deallocate() const
{
    for (int i = 0; i < N; i++)
        delete[] matrix[i];
    delete[] matrix;

    for (int i = 0; i < n; i++)
        delete[] convMatrix[i];
    delete[] convMatrix;
}

void LiniiDinamic::loadData(const vector<vector<int>>& mat,
                            const vector<vector<int>>& conv) const
{
    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            matrix[i][j] = mat[i][j];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            convMatrix[i][j] = conv[i][j];
}

void LiniiDinamic::writeToFile(const char* path) const
{
    ofstream fout(path);
    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < M; j++)
            fout << matrix[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

void LiniiDinamic::calculateRow(const vector<int>& prev,
                                const vector<int>& current,
                                const vector<int>& next,
                                vector<int>& result) const
{
    int offset = n / 2;

    for (int col = 0; col < M; col++)
    {
        long long sum = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int jj = col - offset + j;
                if (jj < 0) jj = 0;
                if (jj >= M) jj = M - 1;

                int val;
                if (i < offset)
                {
                    val = prev[jj];
                }
                else if (i > offset)
                {
                    val = next[jj];
                }
                else
                {
                    val = current[jj];
                }
                sum += (long long)val * convMatrix[i][j];
            }
        }
        result[col] = (int)sum;
    }
}

void LiniiDinamic::worker(int t, int startRow, int endRow, my_barrier &barrier) {
    vector<int> prevRow(M);
    vector<int> currentRow(M);
    vector<int> nextRow(M);
    vector<int> outRow(M);

    vector<int> bufferUp(M), bufferDown(M);

    int upIdx = (startRow == 0) ? 0 : (startRow - 1);
    for (int j = 0; j < M; ++j)
        bufferUp[j] = matrix[upIdx][j];

    int downIdx = (endRow < N) ? endRow : (N - 1);
    for (int j = 0; j < M; ++j)
        bufferDown[j] = matrix[downIdx][j];

    for (int j = 0; j < M; ++j)
        prevRow[j] = bufferUp[j];
    for (int j = 0; j < M; ++j)
        currentRow[j] = matrix[startRow][j]; // prima linie din chunk

    if (startRow + 1 < endRow) {
        for (int j = 0; j < M; ++j)
            nextRow[j] = matrix[startRow + 1][j];
    } else {
        for (int j = 0; j < M; ++j)
            nextRow[j] = bufferDown[j];
    }

    barrier.wait();

    for (int row = startRow; row < endRow; ++row) {
        calculateRow(prevRow, currentRow, nextRow, outRow);
        for (int col = 0; col < M; ++col)
            matrix[row][col] = outRow[col];

        if (row + 1 < endRow) {
            // rotate window
            prevRow.swap(currentRow);
            currentRow.swap(nextRow);

            // load the new 'nextRow'
            if (row + 2 < endRow) {
                for (int col = 0; col < M; ++col)
                    nextRow[col] = matrix[row + 2][col];
            } else {
                for (int col = 0; col < M; ++col)
                    nextRow[col] = bufferDown[col];
            }
        }
    }
}


void LiniiDinamic::run()
{
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

    for (auto& th : threads)
        th.join();
}

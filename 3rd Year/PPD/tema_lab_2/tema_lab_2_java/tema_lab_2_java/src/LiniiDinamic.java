import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class LiniiDinamic {
    private final int N, M, n, p;
    private final int[][] matrix;
    private final int[][] convMatrix;

    public LiniiDinamic(int N, int M, int n, int p) {
        this.N = N;
        this.M = M;
        this.n = n;
        this.p = p;
        this.matrix = new int[N][M];
        this.convMatrix = new int[n][n];
    }

    public void loadData(int[][] srcMatrix, int[][] srcConv) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = srcMatrix[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                convMatrix[i][j] = srcConv[i][j];
            }
        }
    }

    private class Worker implements Runnable {
        private final int threadId;
        private final int totalThreads;
        private final CyclicBarrier barrierStart;
        private final CyclicBarrier barrierEnd;
        private final int[] sharedPrev;
        private final int[] sharedCur;
        private final int[] sharedTemp;

        public Worker(int threadId, int totalThreads,
                      CyclicBarrier barrierStart, CyclicBarrier barrierEnd,
                      int[] sharedPrev, int[] sharedCur, int[] sharedTemp) {
            this.threadId = threadId;
            this.totalThreads = totalThreads;
            this.barrierStart = barrierStart;
            this.barrierEnd = barrierEnd;
            this.sharedPrev = sharedPrev;
            this.sharedCur = sharedCur;
            this.sharedTemp = sharedTemp;
        }

        @Override
        public void run() {
            int offset = n / 2;

            // Calculate column range for this thread once
            int colsPerThread = M / totalThreads;
            int extra = M % totalThreads;
            int startCol = threadId * colsPerThread + Math.min(threadId, extra);
            int endCol = startCol + colsPerThread + (threadId < extra ? 1 : 0);

            try {
                for (int row = 0; row < N; row++) {
                    // Thread 0 prepares sharedCur
                    if (threadId == 0) {
                        for (int j = 0; j < M; j++) {
                            sharedCur[j] = matrix[row][j];
                        }
                    }

                    // Wait for sharedCur to be ready
                    barrierStart.await();

                    // Compute convolution for assigned columns
                    for (int col = startCol; col < endCol; col++) {
                        long sum = 0;

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

                                sum += (long) val * convMatrix[i][j];
                            }
                        }

                        sharedTemp[col] = (int) sum;
                    }

                    // Wait for all threads to finish computing
                    barrierEnd.await();

                    // Copy results back to matrix
                    for (int col = startCol; col < endCol; col++) {
                        matrix[row][col] = sharedTemp[col];
                    }

                    // Thread 0 updates sharedPrev for next iteration
                    if (threadId == 0) {
                        for (int j = 0; j < M; j++) {
                            sharedPrev[j] = sharedCur[j];
                        }
                    }

                    // Ensure sharedPrev is updated before next iteration
                    barrierStart.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() throws InterruptedException {
        CyclicBarrier barrierStart = new CyclicBarrier(p);
        CyclicBarrier barrierEnd = new CyclicBarrier(p);

        int[] sharedPrev = new int[M];
        int[] sharedCur = new int[M];
        int[] sharedTemp = new int[M];

        // Initialize sharedPrev = matrix[0]
        for (int j = 0; j < M; j++) {
            sharedPrev[j] = matrix[0][j];
        }

        Thread[] threads = new Thread[p];

        for (int t = 0; t < p; t++) {
            threads[t] = new Thread(new Worker(t, p, barrierStart, barrierEnd,
                    sharedPrev, sharedCur, sharedTemp));
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
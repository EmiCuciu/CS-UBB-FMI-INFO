import java.io.FileWriter;
import java.io.IOException;
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

    public void loadData(int[][] mat, int[][] conv) {
        for (int i = 0; i < N; i++)
            System.arraycopy(mat[i], 0, matrix[i], 0, M);
        for (int i = 0; i < n; i++)
            System.arraycopy(conv[i], 0, convMatrix[i], 0, n);
    }

    public void writeToFile(String path) throws IOException {
        try (FileWriter fout = new FileWriter(path)) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++)
                    fout.write(matrix[i][j] + " ");
                fout.write("\n");
            }
        }
    }

    private void calculateRow(int[] prev, int[] current, int[] next, int[] result) {
        int offset = n / 2;

        for (int col = 0; col < M; col++) {
            long sum = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int jj = col - offset + j;

                    if (jj < 0) jj = 0;
                    if (jj >= M) jj = M - 1;

                    int val;
                    if (i < offset)
                        val = prev[jj];
                    else if (i > offset)
                        val = next[jj];
                    else
                        val = current[jj];

                    sum += (long) val * convMatrix[i][j];
                }
            }

            result[col] = (int) sum;
        }
    }

    private void worker(int t, int startRow, int endRow, CyclicBarrier barrier) {
        int[] prevRow = new int[M];
        int[] currentRow = new int[M];
        int[] nextRow = new int[M];
        int[] outRow = new int[M];
        int[] bufferUp = new int[M];
        int[] bufferDown = new int[M];

        int upIdx = (startRow == 0) ? 0 : (startRow - 1);
        int downIdx = (endRow < N) ? endRow : (N - 1);

        System.arraycopy(matrix[upIdx], 0, bufferUp, 0, M);
        System.arraycopy(matrix[downIdx], 0, bufferDown, 0, M);
        System.arraycopy(bufferUp, 0, prevRow, 0, M);
        System.arraycopy(matrix[startRow], 0, currentRow, 0, M);

        if (startRow + 1 < endRow)
            System.arraycopy(matrix[startRow + 1], 0, nextRow, 0, M);
        else
            System.arraycopy(bufferDown, 0, nextRow, 0, M);

        try {
            barrier.await();

            for (int row = startRow; row < endRow; row++) {
                calculateRow(prevRow, currentRow, nextRow, outRow);

                System.arraycopy(outRow, 0, matrix[row], 0, M);

                if (row + 1 < endRow) {
                    int[] temp = prevRow;
                    prevRow = currentRow;
                    currentRow = nextRow;
                    nextRow = temp;

                    if (row + 2 < endRow)
                        System.arraycopy(matrix[row + 2], 0, nextRow, 0, M);
                    else
                        System.arraycopy(bufferDown, 0, nextRow, 0, M);
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Thread[] threads = new Thread[p];
        CyclicBarrier barrier = new CyclicBarrier(p);

        int rowsPerThread = N / p;
        int extra = N % p;
        int start = 0;

        for (int t = 0; t < p; ++t) {
            int end = start + rowsPerThread + (extra > 0 ? 1 : 0);
            if (extra > 0) extra--;

            final int threadId = t;
            final int s = start, e = end;
            threads[t] = new Thread(() -> worker(threadId, s, e, barrier));
            threads[t].start();

            start = end;
        }

        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

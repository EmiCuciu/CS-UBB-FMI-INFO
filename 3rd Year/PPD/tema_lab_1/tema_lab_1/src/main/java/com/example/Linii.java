package com.example;

public class Linii {
    int N, M, n, p;
    int[][] matrix;
    int[][] convMatrix;
    int[][] resultMatrix;

    public Linii(int N, int M, int n, int p, int[][] matrix, int[][] convMatrix) {
        this.N = N;
        this.M = M;
        this.n = n;
        this.p = p;
        this.matrix = matrix;
        this.convMatrix = convMatrix;
    }

    public int[][] run() throws InterruptedException {
        resultMatrix = new int[N][M];
        Thread[] threads = new Thread[p];

        int rowsPerThread = N / p;
        int remainingRows = N % p;

        int startingRow = 0;

        for (int threadNr = 0; threadNr < p; threadNr++) {
            int endingRow = startingRow + rowsPerThread;
            if (remainingRows > 0) {
                endingRow++;
                remainingRows--;
            }

            threads[threadNr] = new OrizontalThread(startingRow, endingRow);
            threads[threadNr].start();
            startingRow = endingRow;
        }

        for (int thNr = 0; thNr < p; thNr++) {
            threads[thNr].join();
        }

        return resultMatrix;
    }

    class OrizontalThread extends Thread {
        final private int start, end;

        public OrizontalThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {
            int offset = n / 2;

            for (int row = start; row < end; row++) {
                for (int col = 0; col < M; col++) {
                    int sum = 0;

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            int mtxRow = row - offset + i;
                            int mtxCol = col - offset + j;

                            if (mtxRow < 0) mtxRow = 0;
                            if (mtxRow >= N) mtxRow = N - 1;
                            if (mtxCol < 0) mtxCol = 0;
                            if (mtxCol >= M) mtxCol = M - 1;

                            sum += matrix[mtxRow][mtxCol] * convMatrix[i][j];
                        }
                    }

                    resultMatrix[row][col] = sum;

                }
            }
        }
    }
}

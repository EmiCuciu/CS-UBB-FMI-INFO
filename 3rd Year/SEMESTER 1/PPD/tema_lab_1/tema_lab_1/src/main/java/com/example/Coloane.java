package com.example;

public class Coloane {
    int N, M, n, p;
    int[][] matrix;
    int[][] convMatrix;
    int[][] resultMatrix;

    public Coloane(int N, int M, int n, int p, int[][] matrix, int[][] convMatrix) {
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

        int colsPerThread = M / p;
        int remainingCols = M % p;

        int startCol = 0;

        for (int threadNr = 0; threadNr < p; threadNr++) {
            int endCol = startCol + colsPerThread;
            if (remainingCols > 0) {
                endCol++;
                remainingCols--;
            }

            threads[threadNr] = new VerticalThread(startCol, endCol);
            threads[threadNr].start();
            startCol = endCol;
        }

        for (int thNr = 0; thNr < p; thNr++) {
            threads[thNr].join();
        }

        return resultMatrix;
    }

    class VerticalThread extends Thread {
        private final int startCol, endCol;

        public VerticalThread(int startCol, int endCol) {
            this.startCol = startCol;
            this.endCol = endCol;
        }

        public void run() {
            int offset = n / 2;

            for (int row = 0; row < N; row++) {
                for (int col = startCol; col < endCol; col++) {
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

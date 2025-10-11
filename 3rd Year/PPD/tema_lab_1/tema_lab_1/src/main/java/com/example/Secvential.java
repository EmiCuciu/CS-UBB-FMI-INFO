package com.example;

import java.io.FileWriter;
import java.io.IOException;

public class Secvential {
    int N, M, n;
    int[][] matrix;
    int[][] convMatrix;

//    private static final String OUTPUT_SECV_FILE_DATA = "src/main/java/com/example/data/outputSecvential.txt";
    private static final String OUTPUT_SECV_FILE_DATA = "data/outputSecvential.txt";

    public Secvential(int N, int M, int n, int[][] matrix, int[][] convMatrix) {
        this.N = N;
        this.M = M;
        this.n = n;
        this.matrix = matrix;
        this.convMatrix = convMatrix;
    }

    public int[][] run() {
        int[][] resultMatrix = new int[N][M];
        int offset = n / 2;

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                int sum = 0;

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int ii = row - offset + i;
                        int jj = col - offset + j;

                        //bordam
                        if (ii < 0) ii = 0;
                        if (ii >= N) ii = N - 1;
                        if (jj < 0) jj = 0;
                        if (jj >= M) jj = M - 1;

                        sum += matrix[ii][jj] * convMatrix[i][j];
                    }
                }

                resultMatrix[row][col] = sum;
            }
        }

        writeSecvFile(resultMatrix);

        return resultMatrix;
    }

    private void writeSecvFile(int[][] matrix){
        try (FileWriter writer = new FileWriter(OUTPUT_SECV_FILE_DATA, false)) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(matrix[i][j] + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

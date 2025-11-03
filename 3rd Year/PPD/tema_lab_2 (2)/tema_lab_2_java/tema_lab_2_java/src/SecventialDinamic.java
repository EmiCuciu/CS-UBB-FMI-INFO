public class SecventialDinamic {
    private final int N, M, n;
    private final int[][] matrix;
    private final int[][] convMatrix;

    public SecventialDinamic(int N, int M, int n) {
        this.N = N;
        this.M = M;
        this.n = n;
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

    public void run() {
        int offset = n / 2;
        int[] prevRow = new int[M];

        // Salvam linia precedenta (prima linie)
        for (int j = 0; j < M; j++) {
            prevRow[j] = matrix[0][j];
        }

        for (int row = 0; row < N; row++) {
            int[] currentRow = new int[M];

            // Salvam linia curenta inainte de modificare
            for (int col = 0; col < M; col++) {
                currentRow[col] = matrix[row][col];
            }

            for (int col = 0; col < M; col++) {
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
                            val = prevRow[jj]; // Vecinii de sus (modificati)
                        } else if (ii == row) {
                            val = currentRow[jj];
                        } else {
                            val = matrix[ii][jj];
                        }

                        sum += (long) val * convMatrix[i][j];
                    }
                }

                matrix[row][col] = (int) sum;
            }

            // Pregatim pentru urmatoarea iteratie
            prevRow = currentRow;
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
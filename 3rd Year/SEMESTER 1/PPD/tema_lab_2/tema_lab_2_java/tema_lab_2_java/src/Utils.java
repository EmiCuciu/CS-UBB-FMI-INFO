import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Utils {

    public static class DataInput {
        public int N, M, n, p;
        public int[][] matrix;
        public int[][] convMatrix;

        public DataInput(int N, int M, int n, int p) {
            this.N = N;
            this.M = M;
            this.n = n;
            this.p = p;
            this.matrix = new int[N][M];
            this.convMatrix = new int[n][n];
        }
    }

    public static DataInput readData(String filePath) throws IOException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int N = scanner.nextInt();
            int M = scanner.nextInt();
            int n = scanner.nextInt();
            int p = scanner.nextInt();

            DataInput data = new DataInput(N, M, n, p);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    data.matrix[i][j] = scanner.nextInt();
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data.convMatrix[i][j] = scanner.nextInt();
                }
            }

            return data;
        }
    }

    public static void writeToFile(String path, int[][] matrix) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.print(matrix[i][j]);
                    if (j < matrix[i].length - 1) {
                        writer.print(" ");
                    }
                }
                writer.println();
            }
        }
    }

    public static boolean compareFiles(String file1, String file2) throws IOException {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {

            String line1, line2;
            int lineNumber = 1;

            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                line1 = line1.trim();
                line2 = line2.trim();

                if (!line1.equals(line2)) {
                    System.err.println("Diferenta la linia " + lineNumber);
                    return false;
                }
                lineNumber++;
            }

            if (reader1.readLine() != null || reader2.readLine() != null) {
                System.err.println("Fisierele au numere diferite de linii");
                return false;
            }

            return true;
        }
    }

    public static void generateData(String filePath, int N, int M, int n, int p) throws IOException {
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(N + " " + M + " " + n + " " + p);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    writer.print(random.nextInt(256));
                    if (j < M - 1) {
                        writer.print(" ");
                    }
                }
                writer.println();
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    writer.print(random.nextInt(2));
                    if (j < n - 1) {
                        writer.print(" ");
                    }
                }
                writer.println();
            }

            System.out.println("Fisierul " + filePath + " a fost generat cu succes.");
        }
    }
}
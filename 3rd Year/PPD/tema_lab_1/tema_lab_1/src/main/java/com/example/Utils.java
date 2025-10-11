package com.example;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Utils {

//    private static final String FILE_PATH = "src/main/java/com/example/data/date.txt";
//    private static final String OUTPUT_FILE_PATH = "src/main/java/com/example/data/output.txt";
//    private static final String OUTPUT_SECV_FILE_DATA = "src/main/java/com/example/data/outputSecvential.txt";
//
    private static final String FILE_PATH = "data/date.txt";
    private static final String OUTPUT_FILE_PATH = "data/output.txt";
    private static final String OUTPUT_SECV_FILE_DATA = "data/outputSecvential.txt";


    public static void generateMatrix(int N, int M, int n, int p) {
        Random random = new Random();

        int[][] matrix = new int[N][M];
        int[][] convMatrix = new int[n][n];

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                matrix[row][col] = random.nextInt(100);
            }
        }

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                convMatrix[row][col] = random.nextInt(2);
            }
        }

        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            writer.write(N + "\n" + M + "\n" + n + "\n" + p + "\n");

            for (int[] row : matrix) {
                for (int elem : row) {
                    writer.write(elem + " ");
                }
                writer.write("\n");
            }

            for (int[] row : convMatrix) {
                for (int elem : row) {
                    writer.write(elem + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int[][] getMatrix() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            int N = scanner.nextInt();
            int M = scanner.nextInt();
            scanner.nextInt(); // skip n
            scanner.nextInt(); // skip p
            int[][] matrix = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    matrix[i][j] = scanner.nextInt();
                }
            }
            return matrix;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static int[][] getConvMatrix() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            int N = scanner.nextInt();
            int M = scanner.nextInt();
            int n = scanner.nextInt();
            scanner.nextInt(); // skip p
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    scanner.nextInt(); // skip matrix values
                }
            }
            int[][] convMatrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    convMatrix[i][j] = scanner.nextInt();
                }
            }
            return convMatrix;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static int getN() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            return scanner.nextInt();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static int getM() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            scanner.nextInt(); // skip N
            return scanner.nextInt();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static int getn() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            scanner.nextInt(); // skip N
            scanner.nextInt(); // skip M
            return scanner.nextInt();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static int getp() {
        try (FileReader fileReader = new FileReader(FILE_PATH);
             Scanner scanner = new Scanner(fileReader)) {
            scanner.nextInt(); // skip N
            scanner.nextInt(); // skip M
            scanner.nextInt(); // skip n
            return scanner.nextInt();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static void writeToFile(int[][] matrix) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE_PATH, false)) {
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

    public static boolean compareOutputFiles() {
        try (Scanner scanner1 = new Scanner(new File(OUTPUT_FILE_PATH));
             Scanner scanner2 = new Scanner(new File(OUTPUT_SECV_FILE_DATA))) {

            int rowNumber = 0;
            while (scanner1.hasNextLine() && scanner2.hasNextLine()) {
                rowNumber++;
                String[] line1 = scanner1.nextLine().trim().split("\\s+");
                String[] line2 = scanner2.nextLine().trim().split("\\s+");

                if (line1.length != line2.length) {
                    System.out.println("Row " + rowNumber + " has different lengths.");
                    return false;
                }
                for (int col = 0; col < line1.length; col++) {
                    if (!line1[col].equals(line2[col])) {
                        System.out.println("Mismatch at row " + rowNumber + ", column " + (col + 1));
                        return false;
                    }
                }
            }
            if (scanner1.hasNextLine() || scanner2.hasNextLine()) {
                System.out.println("Files have different number of rows.");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
        System.out.println("The matrices are identical.");
        return true;
    }

}

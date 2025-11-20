package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {
    private static final int NUM_STUDENTS = 200;
    private static final int NUM_FILES = 10;
    private static final int MIN_RECORDS_PER_FILE = 80;
    private static final int MAX_RECORDS_PER_FILE = 150;

    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) {
        Random random = new Random();

        for (int i = 1; i <= NUM_FILES; i++) {
            String filename = PATH + "proiect" + i + ".txt";

            File file = new File(filename);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                int recordsCount = random.nextInt(MAX_RECORDS_PER_FILE - MIN_RECORDS_PER_FILE) + MIN_RECORDS_PER_FILE;

                for (int j = 0; j < recordsCount; j++) {
                    int studentId = random.nextInt(NUM_STUDENTS) + 1;
                    int grade = random.nextInt(10) + 1;

                    writer.write(studentId + "," + grade);
                    writer.newLine();
                }

            } catch (IOException e) {
                System.err.println("Eroare la scrierea fisierului: " + filename);
                e.printStackTrace();
            }
        }
    }
}
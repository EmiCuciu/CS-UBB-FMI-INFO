package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataGenerator {
    private static int NUM_STUDENTS = 500;  // Lab 5: 500 studenti
    private static final int NUM_FILES = 10;  // 10 proiecte

    // Fiecare student are o sansa de a trimite un proiect
    private static final double SUBMISSION_PROBABILITY = 0.85;  // 85% din studenti trimit fiecare proiect

    private static final int MIN_GRADE = 1;
    private static final int MAX_GRADE = 10;

    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                NUM_STUDENTS = Integer.parseInt(args[0]);
                System.out.println("Generare date pentru " + NUM_STUDENTS + " studenti");
            } catch (NumberFormatException e) {
                System.err.println("Argument invalid. Folosim: " + NUM_STUDENTS);
            }
        } else {
            System.out.println("Generare date pentru " + NUM_STUDENTS + " studenti");
        }

        Random random = new Random(42);

        Map<Integer, Double> studentAbility = new HashMap<>();
        for (int i = 1; i <= NUM_STUDENTS; i++) {
            double ability = random.nextGaussian() * 1.5 + 7.0;  // Media 7, deviatia 1.5
            studentAbility.put(i, ability);
        }

        for (int projectNum = 1; projectNum <= NUM_FILES; projectNum++) {
            String filename = PATH + "proiect" + projectNum + ".txt";

            File file = new File(filename);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            double projectDifficulty = 0.5 + (projectNum * 0.5);

            List<String> submissions = new ArrayList<>();

            for (int studentId = 1; studentId <= NUM_STUDENTS; studentId++) {
                if (random.nextDouble() > SUBMISSION_PROBABILITY) {
                    continue;
                }

                double baseGrade = studentAbility.get(studentId);
                double adjustedGrade = baseGrade - (projectDifficulty * 0.3);  // Proiecte mai grele = note mai mici

                adjustedGrade += random.nextGaussian() * 1.0;

                int grade = Math.max(MIN_GRADE, Math.min(MAX_GRADE, (int) Math.round(adjustedGrade)));

                submissions.add(studentId + "," + grade);
            }

            Collections.shuffle(submissions, random);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String submission : submissions) {
                    writer.write(submission);
                    writer.newLine();
                }

                System.out.println("  Proiect " + projectNum + ": " + submissions.size() + " trimiteri" +
                        " (dificultate: " + String.format("%.1f", projectDifficulty) + ")");

            } catch (IOException e) {
                System.err.println("Eroare la scrierea fisierului: " + filename);
                e.printStackTrace();
            }
        }

    }
}
package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Generator de date REALISTE pentru evaluare studenti
 * - 500 studenti (configurat pentru Lab 5)
 * - Note cu distributie normala (media 7, deviatia 2)
 * - Unii studenti nu trimit toate proiectele (realism)
 * - Note mai mari pentru proiecte mai usoare
 */
public class DataGenerator {
    private static int NUM_STUDENTS = 500;  // Lab 5: 500 studenti
    private static final int NUM_FILES = 10;  // 10 proiecte

    // Fiecare student are o sansa de a trimite un proiect
    private static final double SUBMISSION_PROBABILITY = 0.85;  // 85% din studenti trimit fiecare proiect

    // Parametri pentru distributie normala a notelor
    private static final double GRADE_MEAN = 7.0;  // Media: 7
    private static final double GRADE_STD_DEV = 2.0;  // Deviatia standard: 2

    // Note minime/maxime
    private static final int MIN_GRADE = 1;
    private static final int MAX_GRADE = 10;

    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) {
        // Argumentul poate specifica numarul de studenti
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

        Random random = new Random(42);  // Seed pentru reproducibilitate

        // Generam caracteristicile fiecarui student (cat de bun e)
        Map<Integer, Double> studentAbility = new HashMap<>();
        for (int i = 1; i <= NUM_STUDENTS; i++) {
            // Fiecare student are o "abilitate" care influenteaza notele
            double ability = random.nextGaussian() * 1.5 + 7.0;  // Media 7, deviatia 1.5
            studentAbility.put(i, ability);
        }

        int totalSubmissions = 0;

        for (int projectNum = 1; projectNum <= NUM_FILES; projectNum++) {
            String filename = PATH + "proiect" + projectNum + ".txt";

            File file = new File(filename);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            // Dificultatea proiectului (primele proiecte sunt mai usoare)
            double projectDifficulty = 0.5 + (projectNum * 0.5);  // 0.5 -> 5.0

            List<String> submissions = new ArrayList<>();

            // Fiecare student poate trimite proiectul
            for (int studentId = 1; studentId <= NUM_STUDENTS; studentId++) {
                // Nu toti studentii trimit toate proiectele
                if (random.nextDouble() > SUBMISSION_PROBABILITY) {
                    continue;  // Acest student nu trimite acest proiect
                }

                // Generam nota bazata pe abilitatea studentului si dificultatea proiectului
                double baseGrade = studentAbility.get(studentId);
                double adjustedGrade = baseGrade - (projectDifficulty * 0.3);  // Proiecte mai grele = note mai mici

                // Adaugam variatie random (unii au o zi buna/proasta)
                adjustedGrade += random.nextGaussian() * 1.0;

                // Rotunjim si limitam intre MIN si MAX
                int grade = Math.max(MIN_GRADE, Math.min(MAX_GRADE, (int) Math.round(adjustedGrade)));

                submissions.add(studentId + "," + grade);
                totalSubmissions++;
            }

            // Amestecam ordinea (ordinea de trimitere e aleatoare)
            Collections.shuffle(submissions, random);

            // Scriem in fisier
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

        System.out.println("\n✅ Generare completa!");
        System.out.println("📊 Statistici:");
        System.out.println("   - Studenti: " + NUM_STUDENTS);
        System.out.println("   - Proiecte: " + NUM_FILES);
        System.out.println("   - Total trimiteri: " + totalSubmissions);
        System.out.println("   - Medie trimiteri/proiect: " + (totalSubmissions / NUM_FILES));
        System.out.println("   - Distributie: Normal(μ=" + GRADE_MEAN + ", σ=" + GRADE_STD_DEV + ")");
        System.out.println("   - Probabilitate trimitere: " + (SUBMISSION_PROBABILITY * 100) + "%");
    }
}
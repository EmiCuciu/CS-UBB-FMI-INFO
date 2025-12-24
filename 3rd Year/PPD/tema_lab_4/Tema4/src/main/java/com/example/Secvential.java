package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Secvential {
    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10; i++) {
            String filename = PATH + "proiect" + i + ".txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int nota = Integer.parseInt(parts[1]);

                    list.addNota(id, nota);
                }
            } catch (IOException e) {
                System.err.println("Eroare la citire fisier: " + filename);
            }
        }

        long endTime = System.currentTimeMillis();

        // Salvare sortată descrescător după notă
        printToFileSorted(list, PATH + "rezultate.txt");

        System.out.println("Secvential: " + (endTime - startTime) + " ms");
    }

    /**
     * Salvează lista în fișier, sortată descrescător după notă
     */
    private static void printToFileSorted(MyLinkedList list, String filename) {
        // Colectăm toate nodurile într-o listă
        List<StudentRecord> records = new ArrayList<>();
        MyNode current = list.getHead();

        while (current != null) {
            records.add(new StudentRecord(current.id, current.nota));
            current = current.next;
        }

        // Sortăm descrescător după notă (apoi după id pentru stabilitate)
        records.sort((a, b) -> {
            if (a.nota != b.nota) {
                return Integer.compare(b.nota, a.nota); // Descrescător după notă
            }
            return Integer.compare(a.id, b.id); // Crescător după id (stabilitate)
        });

        // Scriem în fișier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (StudentRecord record : records) {
                writer.write(record.id + "," + record.nota);
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write to file: " + filename, ex);
        }
    }

    /**
     * Clasă auxiliară pentru sortare
     */
    private static class StudentRecord {
        final int id;
        final int nota;

        StudentRecord(int id, int nota) {
            this.id = id;
            this.nota = nota;
        }
    }
}
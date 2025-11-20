package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        list.printToFile(PATH + "rezultate.txt");

        System.out.println("Secvential: " + (endTime - startTime) + " ms");
    }
}
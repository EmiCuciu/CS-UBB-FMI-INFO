package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/// Reader
public class Producer implements Runnable {
    private final List<String> filenames;
    private final MyQueue queue;

    public Producer(List<String> filenames, MyQueue queue) {
        this.filenames = filenames;
        this.queue = queue;
    }

    @Override
    public void run() {
        for (var filename : filenames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int nota = Integer.parseInt(parts[1]);

                    Pair p = new Pair(id, nota);

                    queue.add(p);
                }
            } catch (IOException e) {
                System.err.println("Eroare la citire in thread-ul producer: " + filename);
            }
        }
    }
}

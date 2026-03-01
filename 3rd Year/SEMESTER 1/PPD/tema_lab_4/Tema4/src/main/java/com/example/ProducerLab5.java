package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ProducerLab5 implements Callable<Void> {
    private final String filename;
    private final BoundedQueue queue;

    public ProducerLab5(String filename, BoundedQueue queue) {
        this.filename = filename;
        this.queue = queue;
    }

    @Override
    public Void call() throws Exception {
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
            System.err.println("Eroare la citire fisier: " + filename);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer interrupted pentru " + filename);
        } finally {
            // Fiecare producer notifica cand termina fisierul sau
            queue.producerFinished();
        }

        return null;
    }
}


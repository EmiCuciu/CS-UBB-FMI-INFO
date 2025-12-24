package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parallel {
    private static int p = 4;
    private static int p_r = 2;

    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) throws InterruptedException {
        if (args.length >= 1) {
            try {
                p = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument p invalid. Folosim: " + p);
            }
        }

        if (args.length >= 2) {
            try {
                p_r = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument p_r invalid. Folosim: " + p_r);
            }
        }

        int p_w = p - p_r;

        if (p_w <= 0) {
            System.exit(1);
        }

        MyLinkedList linkedList = new MyLinkedList();
        MyQueue queue = new MyQueue();

        List<Thread> producerThreads = new ArrayList<>();
        List<Thread> consumerThreads = new ArrayList<>();


        List<List<String>> fileDistribution = new ArrayList<>();
        for (int i = 0; i < p_r; i++) {
            fileDistribution.add(new ArrayList<>());
        }

        for (int i = 1; i <= 10; i++) {
            String filename = PATH + "proiect" + i + ".txt";
            int target = (i - 1) % p_r;
            fileDistribution.get(target).add(filename);
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < p_r; i++) {
            List<String> fileForThread = fileDistribution.get(i);
            Producer producer = new Producer(fileForThread, queue);
            Thread t = new Thread(producer);
            producerThreads.add(t);

            queue.registerProducer();
            t.start();
        }

        for (int i = 0; i < p_w; i++) {
            Consumer consumer = new Consumer(queue, linkedList);
            Thread t = new Thread(consumer);
            consumerThreads.add(t);
            t.start();
        }

        // Asteptam sa termine toate thread-urile (producatorii si consumatorii au rulat concurent).
        for (Thread t : producerThreads) {
            t.join();
        }

        for (Thread t : consumerThreads) {
            t.join();
        }

        long endTime = System.currentTimeMillis();

        // Salvare sortată descrescător (ca la Secvential și Lab 5)
        printToFileSorted(linkedList, PATH + "rezultate_paralel.txt");

        System.out.println(endTime - startTime);
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

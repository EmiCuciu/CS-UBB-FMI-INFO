package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelLab5 {
    private static int p_r = 4;  // Thread pool size pentru citire
    private static int p_w = 2;  // Numar de workers
    private static int QUEUE_CAPACITY = 100;

    private static final String PATH = "src/main/java/com/example/data/";

    public static void main(String[] args) throws InterruptedException {
        if (args.length >= 1) {
            try {
                p_r = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument p_r invalid. Folosim: " + p_r);
            }
        }

        if (args.length >= 2) {
            try {
                p_w = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument p_w invalid. Folosim: " + p_w);
            }
        }

        if (args.length >= 3) {
            try {
                QUEUE_CAPACITY = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Argument QUEUE_CAPACITY invalid. Folosim: " + QUEUE_CAPACITY);
            }
        }

        long startTime = System.currentTimeMillis();

        // Cream structurile de date
        FineGrainedLinkedList linkedList = new FineGrainedLinkedList();
        BoundedQueue queue = new BoundedQueue(QUEUE_CAPACITY);

        // inregistram toti producatorii inainte de a porni orice thread
        // pentru a evita deadlock-ul când consumatorii văd activeProducers=0
        for (int i = 1; i <= 10; i++) {
            queue.registerProducer();
        }

        //? Cream Executor (Thread Pool) pentru citire
        ExecutorService executorService = Executors.newFixedThreadPool(p_r);

        List<Callable<Void>> readTasks = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            String filename = PATH + "proiect" + i + ".txt";
            ProducerLab5 producer = new ProducerLab5(filename, queue);
            readTasks.add(producer);
        }

        // Pornim workers (consumatori), care vor procesa datele citite
        List<Thread> workerThreads = new ArrayList<>();

        for (int i = 0; i < p_w; i++) {
            ConsumerLab5 consumer = new ConsumerLab5(queue, linkedList);
            Thread t = new Thread(consumer, "Worker-" + i);
            workerThreads.add(t);
            t.start();
        }

        // Executam task-urile de citire in thread pool
        try {
            executorService.invokeAll(readTasks);
        } catch (InterruptedException e) {
            System.err.println("Eroare la executia task-urilor de citire");
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
        }

        // Asteptam workers sa termine procesarea
        for (Thread t : workerThreads) {
            t.join();
        }

        long phaseTime = System.currentTimeMillis();
        System.out.println("Faza 1 (citire + procesare): " + (phaseTime - startTime) + " ms");

        // FAZA 2: Cream lista sortata folosind workers
        List<MyNode> allNodes = linkedList.extractAll();
        SortedLinkedList sortedList = new SortedLinkedList();

        // Folosim un index atomic pentru distributia nodurilor, prevenim race conditions, thread-safe
        AtomicInteger nodeIndex = new AtomicInteger(0);

        // Reutilizam workers pentru sortare
        List<Thread> sortWorkers = new ArrayList<>();

        for (int i = 0; i < p_w; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    int idx = nodeIndex.getAndIncrement();

                    // daca mai sunt noduri de procesat
                    if (idx >= allNodes.size()) {
                        break; // nu mai sunt
                    }

                    MyNode node = allNodes.get(idx);
                    sortedList.insertSorted(node.id, node.nota);
                }
            }, "SortWorker-" + i);

            sortWorkers.add(t);
            t.start();
        }

        // Asteptam finalizarea sortarii
        for (Thread t : sortWorkers) {
            t.join();
        }

        // Salvam rezultatul
        sortedList.printToFile(PATH + "rezultate_paralel_lab5.txt");

        long endTime = System.currentTimeMillis();

        System.out.println("Faza 2 (sortare): " + (endTime - phaseTime) + " ms");
        System.out.println("Timp total: " + (endTime - startTime) + " ms");
        System.out.println("Configuratie: p_r=" + p_r + ", p_w=" + p_w + ", queue_capacity=" + QUEUE_CAPACITY);
    }
}


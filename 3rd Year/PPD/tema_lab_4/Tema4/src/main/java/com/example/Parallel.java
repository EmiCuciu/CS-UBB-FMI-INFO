package com.example;

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

        for (int i = 1; i < 10; i++) {
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
            t.start();
        }

        for (int i = 0; i < p_w; i++) {
            Consumer consumer = new Consumer(queue, linkedList);
            Thread t = new Thread(consumer);
            consumerThreads.add(t);
            t.start();
        }

        //barrier for producers , asteptam sa termine readerii
        for (Thread t : producerThreads) {
            t.join();
        }

        //? anuntam ca am terminat de citit, altfel consumer se afla in starvation
        queue.setProducersFinished();


        for (Thread t : consumerThreads) {
            t.join();
        }

        linkedList.printToFile(PATH + "rezultate_paralel.txt");

        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);

    }
}

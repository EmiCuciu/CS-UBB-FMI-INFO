package com.example;

/// Worker pentru Lab 5 - foloseste BoundedQueue si FineGrainedLinkedList
public class ConsumerLab5 implements Runnable {
    private final BoundedQueue queue;
    private final FineGrainedLinkedList linkedList;

    public ConsumerLab5(BoundedQueue queue, FineGrainedLinkedList linkedList) {
        this.queue = queue;
        this.linkedList = linkedList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pair p = queue.remove();

                if (p == null) {
                    break;
                }

                linkedList.addNota(p.id, p.nota);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Consumer interrupted");
        }
    }
}


package com.example;

public class MyQueue {
    private MyNode head = null;
    private MyNode tail = null;

    private int activeProducers = 0;

    public synchronized void registerProducer() {
        activeProducers++;
    }

    public synchronized void producerFinished() {
        if (activeProducers <= 0) {
            activeProducers = 0;
        } else {
            activeProducers--;
        }
        this.notifyAll();
    }

    public synchronized void add(Pair p) {
        MyNode newNode = new MyNode(p.id, p.nota, null);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode; // adaugam nodul la sfarsit
            tail = newNode;
        }

        this.notifyAll(); // anuntam conumatorii
    }

    public synchronized Pair remove() {
        while (head == null) {
            // coada e vida; putem opri doar daca nu mai exista producatori activi
            if (activeProducers == 0) {
                return null;
            }

            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Pair result = new Pair(head.id, head.nota);

        head = head.next;

        // daca lista devine goala dupa stergere, facem si tail null
        if (head == null) {
            tail = null;
        }

        return result;
    }
}

package com.example;

public class MyQueue {
    private MyNode head = null;
    private MyNode tail = null;

    private boolean producersFinished = false;

    public synchronized void add(Pair p) {
        MyNode newNode = new MyNode(p.id, p.nota, null);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }

        this.notifyAll(); // anuntam conumatorii
    }

    public synchronized Pair remove() {
        while (head == null) {
            // daca coada e goala si nu s-a dat semnal de stop la producerFinished
            if (producersFinished) {
                return null;
            }

            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

    public synchronized void setProducersFinished(){
        this.producersFinished = true;
        this.notifyAll();
    }
}

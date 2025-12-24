package com.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Coada cu capacitate limitata folosind variabile conditionale
 * (Condition variables) pentru producer-consumer pattern
 */
public class BoundedQueue {
    private MyNode head = null;
    private MyNode tail = null;
    private int size = 0;
    private final int capacity;

    private int activeProducers = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();  // pentru producatori
    private final Condition notEmpty = lock.newCondition(); // pentru consumatori

    public BoundedQueue(int capacity) {
        this.capacity = capacity;
    }

    public void registerProducer() {
        lock.lock();
        try {
            activeProducers++;
        } finally {
            lock.unlock();
        }
    }

    public void producerFinished() {
        lock.lock();
        try {
            if (activeProducers > 0) {
                activeProducers--;
            }
            // Anuntam consumatorii ca un producator a terminat
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adauga element in coada (blocking daca e plina)
     */
    public void add(Pair p) throws InterruptedException {
        lock.lock();
        try {
            // Asteptam pana cand coada nu mai e plina
            while (size >= capacity) {
                notFull.await();
            }

            MyNode newNode = new MyNode(p.id, p.nota, null);

            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }

            size++;

            // Anuntam consumatorii ca avem un element nou
            notEmpty.signal();

        } finally {
            lock.unlock();
        }
    }

    /**
     * Extrage element din coada (blocking daca e goala si mai sunt producatori)
     * Returneaza null daca coada e goala si nu mai sunt producatori activi
     */
    public Pair remove() throws InterruptedException {
        lock.lock();
        try {
            while (head == null) {
                // Coada e goala
                if (activeProducers == 0) {
                    // Nu mai sunt producatori, terminam
                    return null;
                }

                // Asteptam sa apara elemente
                notEmpty.await();
            }

            Pair result = new Pair(head.id, head.nota);
            head = head.next;

            if (head == null) {
                tail = null;
            }

            size--;

            // Anuntam producatorii ca s-a eliberat un loc
            notFull.signal();

            return result;

        } finally {
            lock.unlock();
        }
    }
}


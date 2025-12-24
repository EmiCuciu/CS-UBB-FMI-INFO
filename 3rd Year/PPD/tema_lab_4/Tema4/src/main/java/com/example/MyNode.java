package com.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyNode {
    int id;
    int nota;
    MyNode next;
    Lock lock; // Lock pentru fine-grained synchronization

    public MyNode(int id, int nota, MyNode next) {
        this.id = id;
        this.nota = nota;
        this.next = next;
        this.lock = new ReentrantLock();
    }

    // Constructor pentru noduri santinela (fara lock)
    public MyNode(int id, int nota, MyNode next, boolean isSentinel) {
        this.id = id;
        this.nota = nota;
        this.next = next;
        this.lock = isSentinel ? null : new ReentrantLock();
    }
}

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
}

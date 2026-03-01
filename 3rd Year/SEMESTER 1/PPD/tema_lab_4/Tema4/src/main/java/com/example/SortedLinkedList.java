package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SortedLinkedList {
    private final MyNode head;
    private final MyNode tail;

    public SortedLinkedList() {
        head = new MyNode(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
        tail = new MyNode(Integer.MIN_VALUE, Integer.MIN_VALUE, null);
        head.next = tail;
        head.lock = new java.util.concurrent.locks.ReentrantLock();
        tail.lock = new java.util.concurrent.locks.ReentrantLock();
    }

    public void insertSorted(int id, int nota) {
        head.lock.lock();
        MyNode pred = head;

        try {
            MyNode curr = pred.next;
            curr.lock.lock();

            try {
                // Cautam pozitia corecta (descrescator dupa nota)
                while (curr != tail) {
                    if (curr.nota < nota || (curr.nota == nota && curr.id > id)) {
                        // Inseram inainte de curr
                        break;
                    }

                    // Hand-over-hand locking
                    MyNode old = pred;
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                    old.lock.unlock();
                }

                // Inseram nod nou intre pred si curr
                MyNode newNode = new MyNode(id, nota, curr);
                pred.next = newNode;

            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void printToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            MyNode current = head.next;

            while (current != tail) {
                writer.write(current.id + "," + current.nota);
                writer.newLine();
                current = current.next;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write to file: " + filename, ex);
        }
    }
}


package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Lista sortata descrescator dupa nota
 * Foloseste fine-grained synchronization pentru inserare concurenta
 */
public class SortedLinkedList {
    private final MyNode head; // Santinela de inceput
    private final MyNode tail; // Santinela de final

    public SortedLinkedList() {
        // Cream noduri santinela
        head = new MyNode(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
        tail = new MyNode(Integer.MIN_VALUE, Integer.MIN_VALUE, null);
        head.next = tail;
        head.lock = new java.util.concurrent.locks.ReentrantLock();
        tail.lock = new java.util.concurrent.locks.ReentrantLock();
    }

    /**
     * Insereaza un nod in lista sortata descrescator dupa nota
     * Foloseste hand-over-hand locking
     */
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

    /**
     * Salveaza lista in fisier (ordine descrescatoare dupa nota)
     */
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


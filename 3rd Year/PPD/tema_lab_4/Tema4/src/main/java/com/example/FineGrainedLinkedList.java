package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedLinkedList {
    private final MyNode head;
    private final MyNode tail;

    public FineGrainedLinkedList() {
        head = new MyNode(Integer.MIN_VALUE, 0, null);
        tail = new MyNode(Integer.MAX_VALUE, 0, null);
        head.next = tail;
        head.lock = new ReentrantLock();
        tail.lock = new ReentrantLock();
    }

    /**
     * Adauga nota pentru un student sau actualizeaza nota existenta
     * Foloseste hand-over-hand locking pentru a asigura thread-safety
     */
    public void addNota(int id, int nota) {
        head.lock.lock();
        MyNode pred = head;

        try {
            MyNode curr = pred.next;
            curr.lock.lock();

            try {
                while (curr != tail) {
                    if (curr.id == id) {
                        // Gasit - actualizam nota
                        curr.nota += nota;
                        return;
                    } else if (curr.id > id) {
                        // Trebuie sa inseram inainte de curr
                        break;
                    }

                    // Hand-over-hand locking
                    MyNode old = pred;
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                    old.lock.unlock();
                }

                // Nu am gasit id-ul, inseram nod nou intre pred si curr
                pred.next = new MyNode(id, nota, curr);

            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    /**
     * Extrage toate nodurile din lista (exclude santinelele)
     */
    public List<MyNode> extractAll() {
        List<MyNode> nodes = new ArrayList<>();
        MyNode curr = head.next;

        while (curr != tail) {
            nodes.add(curr);
            curr = curr.next;
        }

        return nodes;
    }

}


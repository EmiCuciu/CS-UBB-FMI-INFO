package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Coarse-Grained Synchronization
 */
public class MyLinkedList {
    private MyNode head = null;

    public MyNode getHead() {
        return head;
    }

    public synchronized void addNota(int id, int nota) {
        MyNode current = head;

        while (current != null) {
            if (current.id == id) {
                current.nota += nota;
                return;
            }

            current = current.next;
        }

        this.head = new MyNode(id, nota, head);
    }

    public void printToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            MyNode current = head;

            while (current != null) {
                writer.write(current.id + "," + current.nota);
                writer.newLine();
                current = current.next;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write to file: " + filename, ex);
        }
    }
}

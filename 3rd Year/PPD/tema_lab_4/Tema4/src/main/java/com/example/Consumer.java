package com.example;

/// Worker
public class Consumer implements Runnable{
    private final MyQueue queue;
    private final MyLinkedList linkedList;

    public Consumer(MyQueue queue, MyLinkedList linkedList) {
        this.queue = queue;
        this.linkedList = linkedList;
    }

    @Override
    public void run() {
        while (true) {
            Pair p = queue.remove();

            if (p == null)
                break;

            linkedList.addNota(p.id, p.nota);
        }
    }
}

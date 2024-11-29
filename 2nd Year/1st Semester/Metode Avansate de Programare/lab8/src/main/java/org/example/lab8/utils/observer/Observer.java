package org.example.lab8.utils.observer;


import org.example.lab8.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
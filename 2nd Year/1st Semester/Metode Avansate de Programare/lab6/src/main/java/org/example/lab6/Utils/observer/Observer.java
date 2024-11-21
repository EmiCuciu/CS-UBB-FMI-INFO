package org.example.lab6.Utils.observer;


import org.example.lab6.Utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
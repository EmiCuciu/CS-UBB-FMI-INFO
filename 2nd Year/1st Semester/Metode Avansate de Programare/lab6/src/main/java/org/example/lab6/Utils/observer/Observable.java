package org.example.lab6.Utils.observer;

import org.example.lab6.Utils.events.Event;


public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E e);

}

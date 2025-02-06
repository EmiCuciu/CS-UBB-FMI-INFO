package org.example.grile.Utils.Obs;

import org.example.grile.Utils.Events.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}

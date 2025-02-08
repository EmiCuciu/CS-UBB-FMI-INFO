package org.example.practic.Utils.Obs;

import org.example.practic.Utils.Events.Event;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Event event);
}
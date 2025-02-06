package org.example.ati.Utils.Obs;

import org.example.ati.Utils.Events.Event;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Event event);
}

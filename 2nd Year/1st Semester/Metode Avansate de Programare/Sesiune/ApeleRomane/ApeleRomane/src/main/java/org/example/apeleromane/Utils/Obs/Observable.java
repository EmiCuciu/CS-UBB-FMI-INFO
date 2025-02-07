package org.example.apeleromane.Utils.Obs;

import org.example.apeleromane.Utils.Events.Event;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Event event);
}

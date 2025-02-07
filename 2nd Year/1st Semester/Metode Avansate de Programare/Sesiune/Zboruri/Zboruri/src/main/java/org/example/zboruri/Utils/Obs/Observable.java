package org.example.zboruri.Utils.Obs;

import org.example.zboruri.Utils.Events.Event;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Event event);
}
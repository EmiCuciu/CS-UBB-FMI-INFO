package org.example.ati_v2.Utils.Obs;

import org.example.ati_v2.Utils.Events.Event;

public interface Observable {
    void addObserver(Observer observer);

    void deleteObserver(Observer observer);

    void notifyObservers(Event event);
}

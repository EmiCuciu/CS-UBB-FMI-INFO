package org.example.apeleromane.Utils.Obs;

import org.example.apeleromane.Utils.Events.Event;

public interface Observer {
    void update(Event event);
}
package org.example.practic.Utils.Obs;

import org.example.practic.Utils.Events.Event;

public interface Observer {
    void update(Event event);
}
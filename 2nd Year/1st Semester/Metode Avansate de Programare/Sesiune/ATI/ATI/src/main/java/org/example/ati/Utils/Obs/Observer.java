package org.example.ati.Utils.Obs;

import org.example.ati.Utils.Events.Event;

public interface Observer {
    void update(Event event);
}
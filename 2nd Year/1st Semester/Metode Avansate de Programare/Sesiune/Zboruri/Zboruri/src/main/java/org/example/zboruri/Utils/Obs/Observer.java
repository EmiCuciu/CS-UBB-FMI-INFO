package org.example.zboruri.Utils.Obs;

import org.example.zboruri.Utils.Events.Event;

public interface Observer {
    void update(Event event);
}
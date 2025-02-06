package org.example.grile.Utils.Obs;

import org.example.grile.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}

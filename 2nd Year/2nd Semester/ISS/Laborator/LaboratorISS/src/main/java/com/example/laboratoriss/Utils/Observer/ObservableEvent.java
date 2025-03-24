package com.example.laboratoriss.Utils.Observer;

import java.util.EventObject;

public class ObservableEvent<E> extends EventObject {
    private E data;
    private ChangeEventType type;

    public ObservableEvent(Object source, E data, ChangeEventType type) {
        super(source);
        this.data = data;
        this.type = type;
    }

    public E getData() {
        return data;
    }

    public ChangeEventType getType() {
        return type;
    }
}
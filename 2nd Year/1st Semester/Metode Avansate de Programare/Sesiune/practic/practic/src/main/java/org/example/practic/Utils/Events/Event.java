package org.example.practic.Utils.Events;

public class Event {
    private ChangeEventType type;
    private Object data;

    public Event(ChangeEventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() { return type; }
    public Object getData() { return data; }
}
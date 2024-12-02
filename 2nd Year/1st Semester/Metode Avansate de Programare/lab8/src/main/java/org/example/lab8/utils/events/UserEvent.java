package org.example.lab8.utils.events;

import org.example.lab8.domain.Utilizator;

public class UserEvent implements Event{
    private ChangeEventType type;
    private Utilizator data, old_data;

    public UserEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }

    public UserEvent(ChangeEventType type, Utilizator data, Utilizator old_data) {
        this.type = type;
        this.data = data;
        this.old_data = old_data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return old_data;
    }

}

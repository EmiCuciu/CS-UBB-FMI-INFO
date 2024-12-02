package org.example.lab8.utils.events;

import org.example.lab8.domain.Prietenie;

public class FriendshipEvent implements Event{
    private ChangeEventType type;
    private Prietenie data, oldData;

    public FriendshipEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipEvent(ChangeEventType type, Prietenie data, Prietenie oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getData() {
        return data;
    }

    public Prietenie getOldData() {
        return oldData;
    }

}

package org.example.lab6.Utils.events;


import org.example.lab6.domain.Friendship;

public class FriendshipEntityChangeEvent implements Event {
    private final ChangeEventType type;
    private final Friendship data;
    private Friendship oldData;

    public FriendshipEntityChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipEntityChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
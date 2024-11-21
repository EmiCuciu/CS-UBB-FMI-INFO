package org.example.lab6.Utils.events;


import org.example.lab6.domain.FriendshipRequest;

public class FriendshipRequestEntityChangeEvent implements Event {
    private final ChangeEventType type;
    private FriendshipRequest data;
    private FriendshipRequest oldData;

    public FriendshipRequestEntityChangeEvent(ChangeEventType type, FriendshipRequest data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipRequestEntityChangeEvent(ChangeEventType type, FriendshipRequest data, FriendshipRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }
    public FriendshipRequestEntityChangeEvent(ChangeEventType type){
        this.type = type;

    }
    public ChangeEventType getType() {
        return type;
    }

    public FriendshipRequest getData() {
        return data;
    }

    public FriendshipRequest getOldData() {
        return oldData;
    }
}
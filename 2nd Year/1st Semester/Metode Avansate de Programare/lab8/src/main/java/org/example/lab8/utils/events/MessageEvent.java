package org.example.lab8.utils.events;

import org.example.lab8.domain.Message;

public class MessageEvent implements Event{
    private ChangeEventType type;
    private Message data, oldData;

    public MessageEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }

    public MessageEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }


    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }

}

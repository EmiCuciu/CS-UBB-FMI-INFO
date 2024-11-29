package org.example.lab8.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message replyMessage;

    public ReplyMessage(Long id, Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message reply, Message replyMessage) {
        super(id, from, to, message, data, reply);
        this.replyMessage = replyMessage;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }
}
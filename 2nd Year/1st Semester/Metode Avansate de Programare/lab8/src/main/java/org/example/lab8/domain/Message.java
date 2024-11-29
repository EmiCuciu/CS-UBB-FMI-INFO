package org.example.lab8.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(Long id, Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message reply) {
        this.setId(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = reply;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }
}
package org.example.lab8.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private Long id;
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(Long id, Utilizator from, List<Utilizator> to, String message, LocalDateTime data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = null;
    }

    public Message(Utilizator loggedInUser, List<Utilizator> friends, String message) {
        this.from = loggedInUser;
        this.to = friends;
        this.message = message;
        this.data = LocalDateTime.now();
        this.reply = null;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Utilizator getFromUser() {
        return from;
    }
}
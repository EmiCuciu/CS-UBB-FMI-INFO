package org.example.lab8.domain;

import java.time.LocalDateTime;

public class Prietenie {
    private Tuple<Long, Long> id;
    private LocalDateTime date;
    private String status;

    public Prietenie(LocalDateTime date, String status) {
        this.date = date;
        this.status = status;
    }

    public Prietenie(Long userId, Long friendId) {
        this.id = new Tuple<>(userId, friendId);
    }

    public Tuple<Long, Long> getId() {
        return id;
    }

    public void setId(Tuple<Long, Long> id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
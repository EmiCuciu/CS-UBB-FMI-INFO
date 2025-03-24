package com.example.laboratoriss.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Comanda extends Entity<Integer> {
    private List<ComandaItem> comandaItems;
    private Status status;
    private User user;
    private LocalDateTime data;

    public Comanda(Integer integer, List<ComandaItem> comandaItems, Status status, User user, LocalDateTime data) {
        super(integer);
        this.comandaItems = comandaItems;
        this.status = status;
        this.user = user;
        this.data = data;
    }

    public List<ComandaItem> getComandaItems() {
        return comandaItems;
    }

    public void setComandaItems(List<ComandaItem> comandaItems) {
        this.comandaItems = comandaItems;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "comandaItems=" + comandaItems +
                ", status=" + status +
                ", user=" + user +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comanda that)) return false;
        return getComandaItems().equals(that.getComandaItems()) &&
                getStatus().equals(that.getStatus()) &&
                getUser().equals(that.getUser()) &&
                getData().equals(that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getComandaItems(), getStatus(), getUser(), getData());
    }
}

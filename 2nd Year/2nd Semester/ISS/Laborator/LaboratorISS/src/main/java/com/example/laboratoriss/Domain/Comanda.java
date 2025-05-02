package com.example.laboratoriss.Domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "Comanda")
public class Comanda extends Entity<Integer> {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comanda_id")
    private List<ComandaItem> comandaItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    public Comanda() {
        // Default constructor required by Hibernate
    }

    public Comanda(Integer id, List<ComandaItem> comandaItems, Status status, User user, LocalDateTime data) {
        super(id);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(getId(), comanda.getId()) &&
                Objects.equals(comandaItems, comanda.comandaItems) &&
                status == comanda.status &&
                Objects.equals(user, comanda.user) &&
                Objects.equals(data, comanda.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), comandaItems, status, user, data);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + getId() +
                ", comandaItems=" + comandaItems +
                ", status=" + status +
                ", user=" + user +
                ", data=" + data +
                '}';
    }
}
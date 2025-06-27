package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Player")
public class Player extends Entiti<Integer> {
    private String porecla;

    public Player(Integer id, String porecla) {
        super(id);
        this.porecla = porecla;
    }

    public Player() {
        super(0);
    }

    public String getPorecla() {
        return porecla;
    }

    public void setPorecla(String porecla) {
        this.porecla = porecla;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(porecla, player.porecla);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(porecla);
    }

    @Override
    public String toString() {
        return "Player{" +
                "porecla='" + porecla + '\'' +
                '}';
    }
}

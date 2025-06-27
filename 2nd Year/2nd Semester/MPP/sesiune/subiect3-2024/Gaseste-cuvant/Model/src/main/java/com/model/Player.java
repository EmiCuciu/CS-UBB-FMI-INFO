package com.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Players")
public class Player extends Entiti<Integer>{

    @Column(name = "alias")
    private String alias;

    public Player() {
        super(0);
    }

    public Player(Integer id, String alias){
        super(id);
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(alias, player.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(alias);
    }

    @Override
    public String toString() {
        return "Player{" +
                "alias='" + alias + '\'' +
                '}';
    }
}

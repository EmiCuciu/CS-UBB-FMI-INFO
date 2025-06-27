package com.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Joc")
public class Joc extends Entiti<Integer>{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configuration_id")
    private Configuratie configuratie;

    @Column(name = "starting_time")
    @Convert(converter = LocalDateTime.class)
    private LocalDateTime startingTime;

    private Integer nrOfTries;

    public Joc() {
        super(0);
    }

    public Joc(Integer id, Player player, Configuratie configuratie, LocalDateTime startingTime, Integer nrOfTries) {
        super(id);
        this.player = player;
        this.configuratie = configuratie;
        this.startingTime = startingTime;
        this.nrOfTries = nrOfTries;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Configuratie getConfiguratie() {
        return configuratie;
    }

    public void setConfiguratie(Configuratie configuratie) {
        this.configuratie = configuratie;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public Integer getNrOfTries() {
        return nrOfTries;
    }

    public void setNrOfTries(Integer nrOfTries) {
        this.nrOfTries = nrOfTries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Joc joc = (Joc) o;
        return Objects.equals(player, joc.player) && Objects.equals(configuratie, joc.configuratie) && Objects.equals(startingTime, joc.startingTime) && Objects.equals(nrOfTries, joc.nrOfTries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, configuratie, startingTime, nrOfTries);
    }

    @Override
    public String toString() {
        return "Joc{" +
                "player=" + player +
                ", configuratie=" + configuratie +
                ", startingTime=" + startingTime +
                ", nrOfTries=" + nrOfTries +
                '}';
    }
}

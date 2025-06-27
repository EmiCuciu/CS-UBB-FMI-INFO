package com.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Games")
public class Game extends Entiti<Integer> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    @Column(name = "startingTime")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startingTime;

    @Column(name = "score")
    private Integer score;

    private Integer nrOfCorrectWords;

    public Game(){
        super(0);
    }

    public Game(Integer id, Player player, Configuration configuration, LocalDateTime startingTime, Integer score, Integer nrOfCorrectWords) {
        super(id);
        this.player = player;
        this.configuration = configuration;
        this.startingTime = startingTime;
        this.score = score;
        this.nrOfCorrectWords = nrOfCorrectWords;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getNrOfCorrectWords() {
        return nrOfCorrectWords;
    }

    public void setNrOfCorrectWords(Integer nrOfCorrectWords) {
        this.nrOfCorrectWords = nrOfCorrectWords;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(player, game.player) && Objects.equals(configuration, game.configuration) && Objects.equals(startingTime, game.startingTime) && Objects.equals(score, game.score) && Objects.equals(nrOfCorrectWords, game.nrOfCorrectWords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, configuration, startingTime, score, nrOfCorrectWords);
    }

    @Override
    public String toString() {
        return "Game{" +
                "player=" + player +
                ", configuration=" + configuration +
                ", startingTime=" + startingTime +
                ", score=" + score +
                ", nrOfCorrectWords=" + nrOfCorrectWords +
                '}';
    }
}

package com.services;

import com.model.Game;

import java.io.Serializable;
import java.util.List;

public class GameResult implements Serializable {
    private final Game game;
    private final List<String> allPossibleWords;
    private final int rankingPosition;
    private final List<Game> currentRanking;

    public GameResult(Game game, List<String> allPossibleWords, int rankingPosition, List<Game> currentRanking) {
        this.game = game;
        this.allPossibleWords = allPossibleWords;
        this.rankingPosition = rankingPosition;
        this.currentRanking = currentRanking;
    }

    public Game getGame() {
        return game;
    }

    public List<String> getAllPossibleWords() {
        return allPossibleWords;
    }

    public int getRankingPosition() {
        return rankingPosition;
    }

    public List<Game> getCurrentRanking() {
        return currentRanking;
    }
}
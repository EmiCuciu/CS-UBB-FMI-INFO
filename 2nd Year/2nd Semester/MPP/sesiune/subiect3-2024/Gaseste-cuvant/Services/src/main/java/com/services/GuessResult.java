package com.services;

public class GuessResult implements java.io.Serializable {
    private final boolean correct;
    private final int points;
    private final String message;
    private final boolean gameOver;

    public GuessResult(boolean correct, int points, String message, boolean gameOver) {
        this.correct = correct;
        this.points = points;
        this.message = message;
        this.gameOver = gameOver;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getPoints() {
        return points;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
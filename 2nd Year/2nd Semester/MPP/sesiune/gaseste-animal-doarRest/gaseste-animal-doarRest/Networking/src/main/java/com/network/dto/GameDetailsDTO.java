package com.network.dto;

import java.util.List;

public class GameDetailsDTO {
    private String playerAlias;
    private List<PositionPairDTO> positionPairs;
    private List<String> configuration;
    private int totalScore;
    private long durationSeconds;

    public GameDetailsDTO() {}

    public GameDetailsDTO(String playerAlias, List<PositionPairDTO> positionPairs,
                          List<String> configuration, int totalScore, long durationSeconds) {
        this.playerAlias = playerAlias;
        this.positionPairs = positionPairs;
        this.configuration = configuration;
        this.totalScore = totalScore;
        this.durationSeconds = durationSeconds;
    }

    // Getters and setters
    public String getPlayerAlias() { return playerAlias; }
    public void setPlayerAlias(String playerAlias) { this.playerAlias = playerAlias; }

    public List<PositionPairDTO> getPositionPairs() { return positionPairs; }
    public void setPositionPairs(List<PositionPairDTO> positionPairs) { this.positionPairs = positionPairs; }

    public List<String> getConfiguration() { return configuration; }
    public void setConfiguration(List<String> configuration) { this.configuration = configuration; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
}
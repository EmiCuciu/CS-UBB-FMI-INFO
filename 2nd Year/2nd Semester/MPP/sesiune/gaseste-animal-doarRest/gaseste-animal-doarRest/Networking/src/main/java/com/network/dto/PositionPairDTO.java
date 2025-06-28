package com.network.dto;

public class PositionPairDTO {
    private int firstPosition;
    private int secondPosition;

    public PositionPairDTO() {}

    public PositionPairDTO(int firstPosition, int secondPosition) {
        this.firstPosition = firstPosition;
        this.secondPosition = secondPosition;
    }

    // Getters and setters
    public int getFirstPosition() { return firstPosition; }
    public void setFirstPosition(int firstPosition) { this.firstPosition = firstPosition; }

    public int getSecondPosition() { return secondPosition; }
    public void setSecondPosition(int secondPosition) { this.secondPosition = secondPosition; }
}
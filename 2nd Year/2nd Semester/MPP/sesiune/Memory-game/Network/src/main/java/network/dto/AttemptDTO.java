package network.dto;

import java.time.LocalDateTime;

public class AttemptDTO {
    private Integer position1;
    private Integer position2;
    private LocalDateTime attemptTime;
    private Integer pointsEarned;
    private Boolean matched;

    // Constructors, getters and setters
    public AttemptDTO() {}

    public Integer getPosition1() {
        return position1;
    }

    public void setPosition1(Integer position1) {
        this.position1 = position1;
    }

    public Integer getPosition2() {
        return position2;
    }

    public void setPosition2(Integer position2) {
        this.position2 = position2;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }
}
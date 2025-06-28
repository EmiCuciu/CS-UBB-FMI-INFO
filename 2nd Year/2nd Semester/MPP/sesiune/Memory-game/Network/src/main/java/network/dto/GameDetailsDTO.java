package network.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GameDetailsDTO {
    private Integer id;
    private String playerAlias;
    private List<String> configuration;
    private Integer score;
    private List<AttemptDTO> attempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    // Constructors, getters and setters
    public GameDetailsDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayerAlias() {
        return playerAlias;
    }

    public void setPlayerAlias(String playerAlias) {
        this.playerAlias = playerAlias;
    }

    public List<String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<String> configuration) {
        this.configuration = configuration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<AttemptDTO> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<AttemptDTO> attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
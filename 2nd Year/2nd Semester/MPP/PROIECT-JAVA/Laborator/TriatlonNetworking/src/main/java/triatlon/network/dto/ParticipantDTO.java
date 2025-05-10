package triatlon.network.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ParticipantDTO implements Serializable {
    private Integer id;
    private String first_name;
    private String last_name;
    private Map<TipProbaDTO, Integer> punctaje;
    private Integer totalPoints; // This will be used by JavaFX

    public ParticipantDTO(Integer id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.punctaje = new HashMap<>();
        this.totalPoints = 0; // Initialize with 0
    }

    public Integer getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getPunctajProba(TipProbaDTO tipProba) {
        return punctaje.getOrDefault(tipProba, 0);
    }

    public void setPunctajProba(TipProbaDTO tipProba, int punctaj) {
        punctaje.put(tipProba, punctaj);
        recalculateTotalPoints();
    }

    // Calculate total points from all events
    private void recalculateTotalPoints() {
        int total = 0;
        for (Integer points : punctaje.values()) {
            total += points;
        }
        this.totalPoints = total;
    }

    // JavaFX needs this getter
    public Integer getTotalPoints() {
        return totalPoints;
    }

    @Override
    public String toString() {
        return "ParticipantDTO{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                "}";
    }
}
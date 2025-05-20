package com.example.laborator.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Participant extends Entity<Integer> {
    private String first_name;
    private String last_name;
    private final Map<TipProba, Integer> punctaje;

    public Participant(Integer integer, String first_name, String last_name) {
        super(integer);
        this.first_name = first_name;
        this.last_name = last_name;
        this.punctaje = new HashMap<>();

        for (TipProba tipProba : TipProba.values()) {
            this.punctaje.put(tipProba, 0);
        }
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getPunctajProba(TipProba tipProba) {
        return punctaje.getOrDefault(tipProba, 0);
    }

    public void setPunctajProba(TipProba tipProba, int punctaj) {
        punctaje.put(tipProba, punctaj);
    }

    public int getPunctajTotal() {
        return punctaje.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant that)) return false;
        return getFirst_name().equals(that.getFirst_name()) &&
                getLast_name().equals(that.getLast_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirst_name(), getLast_name());
    }


}

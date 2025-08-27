package triatlon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "Participant")
public class Participant extends Entiti<Integer> {
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Transient
    private Map<TipProba, Integer> punctaje;

    public Participant() {
        super(0);
        punctaje = new HashMap<>();
    }

    public Participant(Integer id, String first_name, String last_name) {
        super(id);
        this.first_name = first_name;
        this.last_name = last_name;
        punctaje = new HashMap<>();
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

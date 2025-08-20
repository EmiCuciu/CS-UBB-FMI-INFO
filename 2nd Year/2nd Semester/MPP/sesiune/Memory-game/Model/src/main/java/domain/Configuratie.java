package domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "Configuratii")
public class Configuratie extends Entity<Integer> {

    @ElementCollection
    @OrderColumn
    private List<String> cuvinte;

    public Configuratie() {}

    public Configuratie(List<String> cuvinte) {
        this.cuvinte = cuvinte;
    }

    public List<String> getCuvinte() {
        return cuvinte;
    }

    public void setCuvinte(List<String> cuvinte) {
        this.cuvinte = cuvinte;
    }

    @Override
    public String toString() {
        return "Configuratie{" +
                "id=" + getId() +
                ", cuvinte=" + cuvinte +
                '}';
    }
}
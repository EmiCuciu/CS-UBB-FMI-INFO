package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "Cuvinte")
public class Cuvant extends Entity<Integer> {

    @Column(name = "text", unique = true)
    private String text;

    public Cuvant() {}

    public Cuvant(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Cuvant{" +
                "id=" + getId() +
                ", text='" + text + '\'' +
                '}';
    }
}
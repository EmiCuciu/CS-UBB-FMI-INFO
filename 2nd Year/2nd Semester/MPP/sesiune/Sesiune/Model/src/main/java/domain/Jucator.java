package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Jucatori")
public class Jucator extends Entity<Integer> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "nume")
    private String nume;

    public Jucator() {}

    public Jucator(String nume) {
        this.nume = nume;
    }

    public Jucator(int id, String nume) {
        this.setId(id);
        this.nume = nume;
    }

    public String getnume() {
        return nume;
    }
    public void setnume(String nume) {
        this.nume = nume;
    }

}

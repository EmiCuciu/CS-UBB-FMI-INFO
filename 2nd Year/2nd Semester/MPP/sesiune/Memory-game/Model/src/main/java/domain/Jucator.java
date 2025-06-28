package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "Jucatori")
public class Jucator extends Entity<Integer>{
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

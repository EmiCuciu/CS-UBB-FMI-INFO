package domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Raspunsuri")
public class RaspunsJucator extends Entity<Integer> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "id_joc")
    private Joc joc;

    @ManyToOne
    @JoinColumn(name = "id_intrebare")
    private Intrebare intrebare;

    @Column(name = "raspuns_jucator")
    private String raspunsJucator;

    @Column(name = "corect")
    private boolean corect;

    @Column(name = "punctaj")
    private int punctaj;

    public RaspunsJucator() {}

    public RaspunsJucator(Joc joc, Intrebare intrebare, String raspunsJucator) {
        this.joc = joc;
        this.intrebare = intrebare;
        this.raspunsJucator = raspunsJucator;
        this.corect = raspunsJucator.equals(intrebare.getRaspunsCorect());
        this.punctaj = calculatePunctaj();
    }

    private int calculatePunctaj() {
        if (corect) {
            return 4 * intrebare.getNivel() * intrebare.getNivel();
        } else {
            return -2;
        }
    }

    // Getters and setters
    public Joc getJoc() {
        return joc;
    }

    public void setJoc(Joc joc) {
        this.joc = joc;
    }

    public Intrebare getIntrebare() {
        return intrebare;
    }

    public void setIntrebare(Intrebare intrebare) {
        this.intrebare = intrebare;
    }

    public String getRaspunsJucator() {
        return raspunsJucator;
    }

    public void setRaspunsJucator(String raspunsJucator) {
        this.raspunsJucator = raspunsJucator;
        this.corect = raspunsJucator.equals(intrebare.getRaspunsCorect());
        this.punctaj = calculatePunctaj();
    }

    public boolean isCorect() {
        return corect;
    }

    public void setCorect(boolean corect) {
        this.corect = corect;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public String toString() {
        return "RaspunsJucator{" +
                "id=" + getId() +
                ", joc=" + (joc != null ? joc.getId() : null) +
                ", intrebare=" + (intrebare != null ? intrebare.getId() : null) +
                ", raspunsJucator='" + raspunsJucator + '\'' +
                ", corect=" + corect +
                ", punctaj=" + punctaj +
                '}';
    }
}
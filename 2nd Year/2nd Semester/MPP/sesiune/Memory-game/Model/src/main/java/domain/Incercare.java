package domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "Incercari")
public class Incercare extends Entity<Integer> {

    @ManyToOne
    @JoinColumn(name = "id_joc")
    private Joc joc;

    @Column(name = "pozitia1")
    private Integer pozitia1;

    @Column(name = "pozitia2")
    private Integer pozitia2;

    @Column(name = "data_incercare")
    private LocalDateTime dataIncercare;

    @Column(name = "puncte_obtinute")
    private Integer puncteObtinute;

    @Column(name = "potrivire")
    private Boolean potrivire;

    public Incercare() {
        this.dataIncercare = LocalDateTime.now();
    }

    public Incercare(Joc joc, Integer pozitia1, Integer pozitia2) {
        this();
        this.joc = joc;
        this.pozitia1 = pozitia1;
        this.pozitia2 = pozitia2;
    }

    public Joc getJoc() {
        return joc;
    }

    public void setJoc(Joc joc) {
        this.joc = joc;
    }

    public Integer getPozitia1() {
        return pozitia1;
    }

    public void setPozitia1(Integer pozitia1) {
        this.pozitia1 = pozitia1;
    }

    public Integer getPozitia2() {
        return pozitia2;
    }

    public void setPozitia2(Integer pozitia2) {
        this.pozitia2 = pozitia2;
    }

    public LocalDateTime getDataIncercare() {
        return dataIncercare;
    }

    public Integer getPuncteObtinute() {
        return puncteObtinute;
    }

    public void setPuncteObtinute(Integer puncteObtinute) {
        this.puncteObtinute = puncteObtinute;
    }

    public Boolean getPotrivire() {
        return potrivire;
    }

    public void setPotrivire(Boolean potrivire) {
        this.potrivire = potrivire;
    }

    @Override
    public String toString() {
        return "Incercare{" +
                "id=" + getId() +
                ", joc=" + joc.getId() +
                ", pozitia1=" + pozitia1 +
                ", pozitia2=" + pozitia2 +
                ", potrivire=" + potrivire +
                ", puncteObtinute=" + puncteObtinute +
                '}';
    }
}
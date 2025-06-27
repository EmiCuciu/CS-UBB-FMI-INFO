package com.model;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "Incercari")
public class Incercare extends Entity<Integer>{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jocId")
    private Joc joc;

    @Column(name = "linie")
    private int linie;

    @Column(name = "coloana")
    private int coloana;

    public Incercare() {}

    public Incercare(Joc joc, int linie, int coloana) {
        this.joc = joc;
        this.linie = linie;
        this.coloana = coloana;
    }

    public Incercare (int id, Joc joc, int linie, int coloana) {
        this.setId(id);
        this.joc = joc;
        this.linie = linie;
        this.coloana = coloana;
    }

    public Joc getJoc() {
        return joc;
    }

    public void setJoc(Joc joc) {
        this.joc = joc;
    }

    public int getLinie() {
        return linie;
    }

    public void setLinie(int linie) {
        this.linie = linie;
    }

    public int getColoana() {
        return coloana;
    }

    public void setColoana(int coloana) {
        this.coloana = coloana;
    }
}

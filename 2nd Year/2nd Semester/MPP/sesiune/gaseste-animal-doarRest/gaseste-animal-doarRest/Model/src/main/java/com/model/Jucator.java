package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "Jucatori")
public class Jucator extends Entity<Integer>{

    @Column(name = "porecla")
    private String porecla;

    public Jucator() {}

    public Jucator(String porecla) {
        this.porecla = porecla;
    }

    public Jucator(int id, String porecla) {
        this.setId(id);
        this.porecla = porecla;
    }

    public String getPorecla() {
        return porecla;
    }

    public void setPorecla(String porecla) {
        this.porecla = porecla;
    }
}

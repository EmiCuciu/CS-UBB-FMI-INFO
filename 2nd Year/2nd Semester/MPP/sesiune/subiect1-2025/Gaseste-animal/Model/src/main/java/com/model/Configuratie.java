package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Configuratie")
public class Configuratie extends Entiti<Integer>{

    @Column(name = "pozitie")
    private Integer pozitie;

    @Column(name = "animal")
    private String animal;

    public Configuratie() {
        super(0);
    }

    public Configuratie(Integer id, Integer pozitie, String animal) {
        super(id);
        this.pozitie = pozitie;
        this.animal = animal;
    }

    public Integer getPozitie() {
        return pozitie;
    }

    public void setPozitie(Integer pozitie) {
        this.pozitie = pozitie;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "Configuratie{" +
                "pozitie=" + pozitie +
                ", animal='" + animal + '\'' +
                '}';
    }
}

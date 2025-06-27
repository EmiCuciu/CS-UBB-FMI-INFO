package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "Configuratii")
public class Configuratie extends Entity<Integer>{

    @Column(name = "linie")
    private int linie;

    @Column(name = "coloana")
    private int coloana;

    @Column(name = "animal")
    private String animal;

    @Column(name = "url_animal")
    private String url_animal;

    public Configuratie(){}

    public Configuratie(int linie,int coloana,String animal,String url_animal){
        this.linie = linie;
        this.coloana = coloana;
        this.animal = animal;
        this.url_animal = url_animal;
    }

    public Configuratie(int id, int linie, int coloana, String animal, String url_animal) {
        this.setId(id);
        this.linie = linie;
        this.coloana = coloana;
        this.animal = animal;
        this.url_animal = url_animal;
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

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getUrl_animal() {
        return url_animal;
    }

    public void setUrl_animal(String url_animal) {
        this.url_animal = url_animal;
    }
}

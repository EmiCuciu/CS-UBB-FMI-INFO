package com.example.laboratoriss.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Medicament")
public class Medicament extends Entiti<Integer> {

    @Column(name = "nume", nullable = false)
    private String nume;

    @Column(name = "pret", nullable = false)
    private Float pret;

    @Column(name = "descriere")
    private String descriere;

    public Medicament() {
        // Default constructor required by Hibernate
    }

    public Medicament(Integer id, String nume, Float pret, String descriere) {
        super(id);
        this.nume = nume;
        this.pret = pret;
        this.descriere = descriere;
    }

    // Existing getters, setters, equals, hashCode, toString methods remain unchanged
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Float getPret() {
        return pret;
    }

    public void setPret(Float pret) {
        this.pret = pret;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
package com.example.laboratoriss.Domain;

import java.util.Objects;

public class Medicament extends Entity<Integer> {
    private String nume;
    private Float pret;
    private String descriere;

    public Medicament(Integer id, String nume, Float pret, String descriere) {
        super(id);
        this.nume = nume;
        this.pret = pret;
        this.descriere = descriere;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medicament that = (Medicament) o;
        return Objects.equals(nume, that.nume) && Objects.equals(pret, that.pret) && Objects.equals(descriere, that.descriere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, pret, descriere);
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "nume='" + nume + '\'' +
                ", pret=" + pret +
                ", descriere='" + descriere + '\'' +
                '}';
    }
}

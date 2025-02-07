package org.example.apeleromane.Domain;

public class Rau extends Entity<Integer>{
    private String nume;
    private double cotaMedie;

    public Rau(Integer id, String nume, double cotaMedie) {
        this.setId(id);
        this.nume = nume;
        this.cotaMedie = cotaMedie;
    }

    public Rau(String nume, double cotaMedie, double cotaMinimaDeRisc, double cotaMaximaAdmisa) {
        this.nume = nume;
        this.cotaMedie = cotaMedie;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getCotaMedie() {
        return cotaMedie;
    }

    public void setCotaMedie(double cotaMedie) {
        this.cotaMedie = cotaMedie;
    }
}

package org.example.apeleromane.Domain;

public class Localitate extends Entity<Integer> {
    private String nume;
    private Rau rau;
    private double cotaMinimaDeRisc;    ///cmdr
    private double cotaMaximaAdmisa;    ///cma

    public Localitate(Integer id, String nume, Rau rau, double cotaMinimaDeRisc, double cotaMaximaAdmisa) {
        this.setId(id);
        this.nume = nume;
        this.rau = rau;
        this.cotaMinimaDeRisc = cotaMinimaDeRisc;
        this.cotaMaximaAdmisa = cotaMaximaAdmisa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Rau getRau() {
        return rau;
    }

    public void setRau(Rau rau) {
        this.rau = rau;
    }

    public double getCotaMinimaDeRisc() {
        return cotaMinimaDeRisc;
    }

    public void setCotaMinimaDeRisc(double cotaMinimaDeRisc) {
        this.cotaMinimaDeRisc = cotaMinimaDeRisc;
    }

    public double getCotaMaximaAdmisa() {
        return cotaMaximaAdmisa;
    }

    public void setCotaMaximaAdmisa(double cotaMaximaAdmisa) {
        this.cotaMaximaAdmisa = cotaMaximaAdmisa;
    }

    @Override
    public String toString() {
        return "Localitate{" +
                "id=" + getId() +
                ", nume='" + nume + '\'' +
                ", rau=" + rau +
                ", cotaMinimaDeRisc=" + cotaMinimaDeRisc +
                ", cotaMaximaAdmisa=" + cotaMaximaAdmisa +
                '}';
    }

    public TipCotaPericol calculeazaRisc() {
        double nivelRau = rau.getCotaMedie();
        if (nivelRau > cotaMaximaAdmisa){
            return TipCotaPericol.MAJOR;
        }
        else if (nivelRau > cotaMinimaDeRisc){
            return TipCotaPericol.MEDIU;
        }
        else {
            return TipCotaPericol.REDUS;
        }
    }
}
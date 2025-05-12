package com.example.laborator.Domain;

import java.util.Objects;

public class Proba extends Entity<Integer> {
    private TipProba tipProba;
    private Arbitru arbitru;

    public Proba(Integer integer, TipProba tipProba, Arbitru arbitru) {
        super(integer);
        this.tipProba = tipProba;
        this.arbitru = arbitru;
    }

    public TipProba getTipProba() {
        return tipProba;
    }

    public void setTipProba(TipProba tipProba) {
        this.tipProba = tipProba;
    }

    public Arbitru getArbitru() {
        return arbitru;
    }

    public void setArbitru(Arbitru arbitru) {
        this.arbitru = arbitru;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Proba proba = (Proba) o;
        return tipProba == proba.tipProba && Objects.equals(getArbitru(), proba.getArbitru());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipProba, getArbitru());
    }

    @Override
    public String toString() {
        return "Proba{" +
                "tipProba='" + tipProba + '\'' +
                ", arbitru=" + arbitru +
                '}';
    }
}

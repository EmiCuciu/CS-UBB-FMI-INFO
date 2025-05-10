package com.example.laborator.Domain;

public enum TipProba {
    NATATIE("Natatie"),
    CICLISM("Ciclism"),
    ALERGARE("Alergare");

    private final String denumire;

    TipProba(String denumire) {
        this.denumire = denumire;
    }

    public String getDenumire() {
        return denumire;
    }

    @Override
    public String toString() {
        return denumire;
    }

    public TipProba getTipProba() {
        return this;
    }
}

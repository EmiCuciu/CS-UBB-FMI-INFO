package com.example.laboratoriss.Domain;

import java.util.Objects;

public class ComandaItem extends Entity<Integer> {
    private Medicament medicament;
    private Integer cantitate;

    public ComandaItem(Integer integer, Medicament medicament, Integer cantitate) {
        super(integer);
        this.medicament = medicament;
        this.cantitate = cantitate;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ComandaItem that = (ComandaItem) o;
        return Objects.equals(medicament, that.medicament) && Objects.equals(cantitate, that.cantitate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicament, cantitate);
    }

    @Override
    public String toString() {
        return "ComandaItem{" +
                "medicament=" + medicament +
                ", cantitate=" + cantitate +
                '}';
    }
}

package com.example.laboratoriss.Domain;

import javax.persistence.*;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "ComandaItem")
public class ComandaItem extends Entity<Integer> {

    @ManyToOne
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

    @Column(name = "cantitate", nullable = false)
    private Integer cantitate;

    public ComandaItem() {
        // Default constructor required by Hibernate
    }

    public ComandaItem(Integer id, Medicament medicament, Integer cantitate) {
        super(id);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComandaItem that = (ComandaItem) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(medicament, that.medicament) &&
                Objects.equals(cantitate, that.cantitate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), medicament, cantitate);
    }

    @Override
    public String toString() {
        return "ComandaItem{" +
                "id=" + getId() +
                ", medicament=" + medicament +
                ", cantitate=" + cantitate +
                '}';
    }
}
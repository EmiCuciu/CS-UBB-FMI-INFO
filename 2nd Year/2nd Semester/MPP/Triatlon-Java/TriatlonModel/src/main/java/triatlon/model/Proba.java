package triatlon.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Proba")
public class Proba extends Entiti<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "tip_proba")
    private TipProba tipProba;

    @ManyToOne(fetch = FetchType.EAGER) //? sa se incarce deodata cu Arbitrul
    @JoinColumn(name = "arbitru_id")
    private Arbitru arbitru;

    public Proba() {
        super(0);
    }

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

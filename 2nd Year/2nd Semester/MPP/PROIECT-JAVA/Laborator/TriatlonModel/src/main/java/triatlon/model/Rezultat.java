package triatlon.model;

import java.util.Objects;

public class Rezultat extends Entity<Integer> {
    private Participant participant;
    private Arbitru arbitru;
    private TipProba tipProba;
    private int punctaj;

    public Rezultat(Integer integer, Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) {
        super(integer);
        this.participant = participant;
        this.arbitru = arbitru;
        this.tipProba = tipProba;
        this.punctaj = punctaj;
    }

    public Rezultat(Integer integer, Participant participant, Arbitru arbitru, String tipProba, int punctaj) {
        super(integer);
        this.participant = participant;
        this.arbitru = arbitru;
        this.tipProba = TipProba.valueOf(tipProba);
        this.punctaj = punctaj;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Arbitru getArbitru() {
        return arbitru;
    }

    public void setArbitru(Arbitru arbitru) {
        this.arbitru = arbitru;
    }

    public TipProba getTipProba() {
        return tipProba;
    }

    public String getTipProbaString(){
        return tipProba.toString();
    }

    public void setTipProba(TipProba tipProba) {
        this.tipProba = tipProba;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public String toString() {
        return "Rezultat{" +
                "participant=" + participant +
                ", tipProba=" + tipProba +
                ", punctaj=" + punctaj +
                ", arbitru=" + arbitru +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rezultat rezultat = (Rezultat) o;
        return getPunctaj() == rezultat.getPunctaj() &&
                Objects.equals(getParticipant(), rezultat.getParticipant()) &&
                tipProba == rezultat.tipProba &&
                Objects.equals(getArbitru(), rezultat.getArbitru());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParticipant(), tipProba, getPunctaj(), getArbitru());
    }
}

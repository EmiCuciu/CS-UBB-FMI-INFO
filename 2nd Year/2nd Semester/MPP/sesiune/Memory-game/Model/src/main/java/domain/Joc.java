package domain;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "Jocuri")
public class Joc extends Entity<Integer> {

    @ManyToOne
    @JoinColumn(name = "id_jucator")
    private Jucator jucator;

    @ManyToOne
    @JoinColumn(name = "id_configuratie")
    private Configuratie configuratie;

    @Column(name = "data_inceput")
    private LocalDateTime dataInceput;

    @Column(name = "data_sfarsit")
    private LocalDateTime dataSfarsit;

    @Column(name = "punctaj")
    private Integer punctaj;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusJoc status;

    public enum StatusJoc {
        IN_PROGRES, FINALIZAT
    }

    public Joc() {
        this.dataInceput = LocalDateTime.now();
        this.punctaj = 0;
        this.status = StatusJoc.IN_PROGRES;
    }

    public Joc(Jucator jucator, Configuratie configuratie) {
        this();
        this.jucator = jucator;
        this.configuratie = configuratie;
    }

    public Jucator getJucator() {
        return jucator;
    }

    public void setJucator(Jucator jucator) {
        this.jucator = jucator;
    }

    public Configuratie getConfiguratie() {
        return configuratie;
    }

    public void setConfiguratie(Configuratie configuratie) {
        this.configuratie = configuratie;
    }

    public LocalDateTime getDataInceput() {
        return dataInceput;
    }

    public LocalDateTime getDataSfarsit() {
        return dataSfarsit;
    }

    public void finalizeazaJoc() {
        this.dataSfarsit = LocalDateTime.now();
        this.status = StatusJoc.FINALIZAT;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void adaugaPunctaj(Integer puncte) {
        this.punctaj += puncte;
    }

    public StatusJoc getStatus() {
        return status;
    }

    public long getDurataInSecunde() {
        if (dataSfarsit == null) {
            return Duration.between(dataInceput, LocalDateTime.now()).getSeconds();
        }
        return Duration.between(dataInceput, dataSfarsit).getSeconds();
    }

    @Override
    public String toString() {
        return "Joc{" +
                "id=" + getId() +
                ", jucator=" + jucator.getnume() +
                ", punctaj=" + punctaj +
                ", status=" + status +
                ", durata=" + getDurataInSecunde() + "s" +
                '}';
    }
}
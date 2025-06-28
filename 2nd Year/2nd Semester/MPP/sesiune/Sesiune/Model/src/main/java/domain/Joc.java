package domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "Jocuri")
public class Joc extends Entity<Integer> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "id_jucator")
    private Jucator jucator;

    @Column(name = "data_inceput")
    private LocalDateTime dataInceput;

    @Column(name = "data_sfarsit")
    private LocalDateTime dataSfarsit;

    @Column(name = "punctaj_total")
    private int punctajTotal;

    @Column(name = "finalizat_cu_succes")
    private boolean finalizatCuSucces;

    @Column(name = "nivel_curent")
    private int nivelCurent;

    @Column(name = "intrebari_corecte_nivel")
    private int intrebariCorecteNivel;

    public Joc() {
        this.dataInceput = LocalDateTime.now();
        this.punctajTotal = 0;
        this.finalizatCuSucces = false;
        this.nivelCurent = 1;
        this.intrebariCorecteNivel = 0;
    }

    public Joc(Jucator jucator) {
        this();
        this.jucator = jucator;
    }

    // Getters and setters
    public Jucator getJucator() {
        return jucator;
    }

    public void setJucator(Jucator jucator) {
        this.jucator = jucator;
    }

    public LocalDateTime getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(LocalDateTime dataInceput) {
        this.dataInceput = dataInceput;
    }

    public LocalDateTime getDataSfarsit() {
        return dataSfarsit;
    }

    public void setDataSfarsit(LocalDateTime dataSfarsit) {
        this.dataSfarsit = dataSfarsit;
    }

    public int getPunctajTotal() {
        return punctajTotal;
    }

    public void setPunctajTotal(int punctajTotal) {
        this.punctajTotal = punctajTotal;
    }

    public boolean isFinalizatCuSucces() {
        return finalizatCuSucces;
    }

    public void setFinalizatCuSucces(boolean finalizatCuSucces) {
        this.finalizatCuSucces = finalizatCuSucces;
    }

    public int getNivelCurent() {
        return nivelCurent;
    }

    public void setNivelCurent(int nivelCurent) {
        this.nivelCurent = nivelCurent;
    }

    public int getIntrebariCorecteNivel() {
        return intrebariCorecteNivel;
    }

    public void setIntrebariCorecteNivel(int intrebariCorecteNivel) {
        this.intrebariCorecteNivel = intrebariCorecteNivel;
    }

    public long getDurata() {
        if (dataSfarsit == null) {
            return -1;
        }
        return java.time.Duration.between(dataInceput, dataSfarsit).getSeconds();
    }

    @Override
    public String toString() {
        return "Joc{" +
                "id=" + getId() +
                ", jucator=" + jucator +
                ", dataInceput=" + dataInceput +
                ", dataSfarsit=" + dataSfarsit +
                ", punctajTotal=" + punctajTotal +
                ", finalizatCuSucces=" + finalizatCuSucces +
                ", nivelCurent=" + nivelCurent +
                ", intrebariCorecteNivel=" + intrebariCorecteNivel +
                '}';
    }
}
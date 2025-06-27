package com.model;

import jakarta.persistence.*;

import java.util.Date;

@jakarta.persistence.Entity
@Table(name = "Jocuri")
public class Joc extends Entity<Integer>{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jucatorId")
    private Jucator jucator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configuratieId")
    private Configuratie configuratie;

    @Column(name = "startTime")
    private Date startTime;

    @Column(name = "nrIncercari")
    private Integer nrIncercari;

    @Column(name = "ghicit")
    private boolean ghicit;

    public Joc() {
    }

    public Joc(Jucator jucator, Configuratie configuratie, Date startTime, Integer nrIncercari, boolean ghicit) {
        this.jucator = jucator;
        this.configuratie = configuratie;
        this.startTime = startTime;
        this.nrIncercari = nrIncercari;
        this.ghicit = ghicit;
    }

    public Joc(int id, Jucator jucator, Configuratie configuratie, Date startTime, Integer nrIncercari, boolean ghicit) {
        this.setId(id);
        this.jucator = jucator;
        this.configuratie = configuratie;
        this.startTime = startTime;
        this.nrIncercari = nrIncercari;
        this.ghicit = ghicit;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getNrIncercari() {
        return nrIncercari;
    }

    public void setNrIncercari(Integer nrIncercari) {
        this.nrIncercari = nrIncercari;
    }

    public boolean isGhicit() {
        return ghicit;
    }

    public void setGhicit(boolean ghicit) {
        this.ghicit = ghicit;
    }
}

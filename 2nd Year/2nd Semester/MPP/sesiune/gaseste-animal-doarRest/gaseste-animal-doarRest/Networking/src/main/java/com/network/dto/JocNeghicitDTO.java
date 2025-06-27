package com.network.dto;

import java.util.List;

public class JocNeghicitDTO {
    private int idJoc;
    private int nrIncercari;
    private List<Integer> pozitiiPropuse;
    private int pozAnimal;

    public JocNeghicitDTO(int idJoc, int nrIncercari, List<Integer> pozitiiPropuse, int pozAnimal) {
        this.idJoc = idJoc;
        this.nrIncercari = nrIncercari;
        this.pozitiiPropuse = pozitiiPropuse;
        this.pozAnimal = pozAnimal;
    }

    public int getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(int idJoc) {
        this.idJoc = idJoc;
    }

    public int getNrIncercari() {
        return nrIncercari;
    }

    public void setNrIncercari(int nrIncercari) {
        this.nrIncercari = nrIncercari;
    }

    public List<Integer> getPozitiiPropuse() {
        return pozitiiPropuse;
    }

    public void setPozitiiPropuse(List<Integer> pozitiiPropuse) {
        this.pozitiiPropuse = pozitiiPropuse;
    }

    public int getPozAnimal() {
        return pozAnimal;
    }

    public void setPozAnimal(int pozAnimal) {
        this.pozAnimal = pozAnimal;
    }
}

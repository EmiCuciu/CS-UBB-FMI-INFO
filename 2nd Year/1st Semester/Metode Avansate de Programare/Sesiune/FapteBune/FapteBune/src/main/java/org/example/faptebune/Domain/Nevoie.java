package org.example.faptebune.Domain;

public class Nevoie extends Entity<Long> {
    private String titlu;
    private String descriere;
    private String deadline;
    private long omInNevoie;
    private Long omSalvator;
    private String status;

    public Nevoie(Long id, String titlu, String descriere, String deadline, Persoana omInNevoie, Persoana omSalvator, String status) {
        this.setId(id);
        this.titlu = titlu;
        this.descriere = descriere;
        this.deadline = deadline;
        this.omInNevoie = omInNevoie.getId();
        this.omSalvator = omSalvator.getId();
        this.status = status;
    }

    public Nevoie() {
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public long getOmInNevoie() {
        return omInNevoie;
    }

    public void setOmInNevoie(long omInNevoie) {
        this.omInNevoie = omInNevoie;
    }

    public Long getOmSalvator() {
        return omSalvator;
    }

    public void setOmSalvator(Long omSalvator) {
        this.omSalvator = omSalvator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

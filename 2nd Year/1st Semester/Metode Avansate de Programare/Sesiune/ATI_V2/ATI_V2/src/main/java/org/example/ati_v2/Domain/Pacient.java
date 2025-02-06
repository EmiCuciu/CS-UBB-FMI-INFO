package org.example.ati_v2.Domain;

public class Pacient extends Entity<String>{
    private int varstaLuni;
    private boolean prematur;
    private String diagnostic;
    private int gravitate;
    private StatusPacient statusPacient;

    public Pacient(String cnp, int varstaLuni, boolean prematur, String diagnostic, int gravitate) {
        this.setId(cnp);
        this.varstaLuni = varstaLuni;
        this.prematur = prematur;
        this.diagnostic = diagnostic;
        setGravitate(gravitate);
        this.statusPacient = StatusPacient.IN_ASTEPTARE;
    }

    public int getVarstaLuni() {
        return varstaLuni;
    }

    public void setVarstaLuni(int varstaLuni) {
        this.varstaLuni = varstaLuni;
    }

    public boolean isPrematur() {
        return prematur;
    }

    public void setPrematur(boolean prematur) {
        this.prematur = prematur;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public int getGravitate() {
        return gravitate;
    }

    public void setGravitate(int gravitate) {
        if(gravitate < 1 || gravitate > 14){
            throw new IllegalArgumentException("Gravitatea trebuie sa fie intre 1 si 14");
        }
        this.gravitate = gravitate;
    }

    public StatusPacient getStatusPacient() {
        return statusPacient;
    }

    public void setStatusPacient(StatusPacient statusPacient) {
        this.statusPacient = statusPacient;
    }
}

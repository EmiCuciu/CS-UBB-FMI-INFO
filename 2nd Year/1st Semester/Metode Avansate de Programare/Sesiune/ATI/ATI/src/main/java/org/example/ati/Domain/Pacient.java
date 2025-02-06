package org.example.ati.Domain;


public class Pacient extends Entity<String> {
    private int varsteLuni;
    private boolean prematur;
    private String diagnosticPrincipal;
    private int gravitate;
    private StatusPacient status;

    public Pacient(String cnp, int varsteLuni, boolean prematur, String diagnosticPrincipal, int gravitate) {
        this.setId(cnp);
        this.varsteLuni = varsteLuni;
        this.prematur = prematur;
        this.diagnosticPrincipal = diagnosticPrincipal;
        setGravitate(gravitate);
        this.status = StatusPacient.IN_ASTEPTARE;
    }

    public int getVarsteLuni() {
        return varsteLuni;
    }

    public boolean isPrematur() {
        return prematur;
    }

    public String getDiagnosticPrincipal() {
        return diagnosticPrincipal;
    }

    public int getGravitate() {
        return gravitate;
    }

    public void setGravitate(int gravitate) {
        if (gravitate < 1 || gravitate > 14) {
            throw new IllegalArgumentException("Gravitatea trebuie să fie între 1 și 14");
        }
        this.gravitate = gravitate;
    }

    public StatusPacient getStatus() {
        return status;
    }

    public void setStatus(StatusPacient status) {
        this.status = status;
    }
}

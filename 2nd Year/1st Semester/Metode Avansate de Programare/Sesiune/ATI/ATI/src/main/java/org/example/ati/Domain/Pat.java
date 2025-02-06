package org.example.ati.Domain;

public class Pat extends Entity<Integer> {
    private TipPat tip;
    private boolean ventilatie;
    private String pacientCnp;

    public Pat(Integer id, TipPat tip, boolean ventilatie) {
        this.setId(id);
        this.tip = tip;
        this.ventilatie = ventilatie;
        this.pacientCnp = null;
    }

    public TipPat getTip() {
        return tip;
    }

    public void setTip(TipPat tip) {
        this.tip = tip;
    }

    public boolean hasVentilatie() {
        return ventilatie;
    }

    public void setVentilatie(boolean ventilatie) {
        this.ventilatie = ventilatie;
    }

    public String getPacientCnp() {
        return pacientCnp;
    }

    public void setPacientCnp(String pacientCnp) {
        this.pacientCnp = pacientCnp;
    }

    public boolean isLiber() {
        return pacientCnp == null;
    }
}

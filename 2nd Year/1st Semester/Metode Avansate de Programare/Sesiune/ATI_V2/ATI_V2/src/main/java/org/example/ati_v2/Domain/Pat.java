package org.example.ati_v2.Domain;

public class Pat extends Entity<Integer>{
    private TipPat tip;
    private boolean ventilatie;
    private String pacientCNP;

    public Pat(Integer id, TipPat tip, boolean ventilatie){
        this.setId(id);
        this.tip = tip;
        this.ventilatie = ventilatie;
        this.pacientCNP = null;
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

    public String getPacientCNP() {
        return pacientCNP;
    }

    public void setPacientCNP(String pacientCNP) {
        this.pacientCNP = pacientCNP;
    }

    public boolean isLiber(){
        return pacientCNP == null;
    }
}

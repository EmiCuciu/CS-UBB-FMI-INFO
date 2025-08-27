package triatlon.network.dto;

import java.io.Serializable;

public class ProbaDTO implements Serializable {
    private Integer id;
    private TipProbaDTO tipProba;
    private ArbitruDTO arbitru;

    public ProbaDTO(Integer id, TipProbaDTO tipProba, ArbitruDTO arbitru) {
        this.id = id;
        this.tipProba = tipProba;
        this.arbitru = arbitru;
    }

    public Integer getId() {
        return id;
    }

    public TipProbaDTO getTipProba() {
        return tipProba;
    }

    public ArbitruDTO getArbitru() {
        return arbitru;
    }

    public void setTipProba(TipProbaDTO tipProba) {
        this.tipProba = tipProba;
    }
}
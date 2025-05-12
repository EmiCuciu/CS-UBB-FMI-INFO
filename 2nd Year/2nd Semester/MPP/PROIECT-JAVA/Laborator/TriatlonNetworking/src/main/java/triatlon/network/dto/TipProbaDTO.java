package triatlon.network.dto;

import java.io.Serializable;

public enum TipProbaDTO implements Serializable {
    NATATIE("Natatie"),
    CICLISM("Ciclism"),
    ALERGARE("Alergare");

    private final String denumire;

    TipProbaDTO(String denumire) {
        this.denumire = denumire;
    }

    public String getDenumire() {
        return denumire;
    }
}
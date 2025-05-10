package triatlon.network.dto;

import java.io.Serializable;

public class RezultatDTO implements Serializable {
    private Integer id;
    private ParticipantDTO participant;
    private ArbitruDTO arbitru;
    private TipProbaDTO tipProba;
    private int punctaj;

    public RezultatDTO(Integer id, ParticipantDTO participant, ArbitruDTO arbitru, TipProbaDTO tipProba, int punctaj) {
        this.id = id;
        this.participant = participant;
        this.arbitru = arbitru;
        this.tipProba = tipProba;
        this.punctaj = punctaj;
    }

    public Integer getId() {
        return id;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public ArbitruDTO getArbitru() {
        return arbitru;
    }

    public TipProbaDTO getTipProba() {
        return tipProba;
    }

    public int getPunctaj() {
        return punctaj;
    }
}
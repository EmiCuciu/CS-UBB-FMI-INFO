package triatlon.network.jsonprotocol;

import triatlon.model.Participant;
import triatlon.network.dto.*;
import triatlon.model.Rezultat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;

    // Individual DTOs
    private ArbitruDTO arbitruDTO;
    private ParticipantDTO participantDTO;
    private ProbaDTO probaDTO;
    private RezultatDTO rezultatDTO;

    // Arrays of DTOs
    private ArbitruDTO[] arbitriiDTO;
    private ParticipantDTO[] participantiDTO;
    private ProbaDTO[] probeDTO;
    private RezultatDTO[] rezultateDTO;

    public Response() {
    }

    // Getters and setters for type and error message
    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters and setters for individual DTOs
    public ArbitruDTO getArbitruDTO() {
        return arbitruDTO;
    }

    public void setArbitruDTO(ArbitruDTO arbitruDTO) {
        this.arbitruDTO = arbitruDTO;
    }

    public ParticipantDTO getParticipantDTO() {
        return participantDTO;
    }

    public void setParticipantDTO(ParticipantDTO participantDTO) {
        this.participantDTO = participantDTO;
    }

    public ProbaDTO getProbaDTO() {
        return probaDTO;
    }

    public void setProbaDTO(ProbaDTO probaDTO) {
        this.probaDTO = probaDTO;
    }

    public RezultatDTO getRezultatDTO() {
        return rezultatDTO;
    }

    public void setRezultatDTO(RezultatDTO rezultatDTO) {
        this.rezultatDTO = rezultatDTO;
    }

    // Getters and setters for arrays of DTOs
    public ArbitruDTO[] getArbitrii() {
        return arbitriiDTO;
    }

    public void setArbitrii(ArbitruDTO[] arbitriiDTO) {
        this.arbitriiDTO = arbitriiDTO;
    }

    public ParticipantDTO[] getParticipanti() {
        return participantiDTO;
    }

    public void setParticipanti(ParticipantDTO[] participantiDTO) {
        this.participantiDTO = participantiDTO;
    }

    public ProbaDTO[] getProbe() {
        return probeDTO;
    }

    public void setProbe(ProbaDTO[] probeDTO) {
        this.probeDTO = probeDTO;
    }

    public RezultatDTO[] getRezultateDTO() {
        return rezultateDTO;
    }

    public void setRezultateDTO(RezultatDTO[] rezultateDTO) {
        this.rezultateDTO = rezultateDTO;
    }

    // Conversion method that correctly uses DTOUtils
    public Rezultat[] getRezultate() {
        if (rezultateDTO == null) {
            return null;
        }
        return DTOUtils.getFromDTO(rezultateDTO);
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", arbitruDTO=" + arbitruDTO +
                ", arbitriiDTO=" + Arrays.toString(arbitriiDTO) +
                ", participantDTO=" + participantDTO +
                ", participantiDTO=" + Arrays.toString(participantiDTO) +
                ", probaDTO=" + probaDTO +
                ", probeDTO=" + Arrays.toString(probeDTO) +
                ", rezultatDTO=" + rezultatDTO +
                ", rezultateDTO=" + Arrays.toString(rezultateDTO) +
                '}';
    }

    public TipProbaDTO getTipProba() {
        if (probaDTO == null) {
            return null;
        }
        return probaDTO.getTipProba();
    }


    public void setTipProba(TipProbaDTO tipProba) {
        if (probaDTO != null) {
            probaDTO.setTipProba(tipProba);
        }
    }
}
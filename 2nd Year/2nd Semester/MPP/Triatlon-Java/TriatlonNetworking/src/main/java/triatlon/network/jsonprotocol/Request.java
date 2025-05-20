package triatlon.network.jsonprotocol;


import triatlon.network.dto.*;

public class Request {
    private RequestType type;
    private ArbitruDTO arbitruDTO;
    private ParticipantDTO participantDTO;
    private RezultatDTO rezultatDTO;
    private ProbaDTO probaDTO;
    private TipProbaDTO tipProbaDTO;

    public Request() {
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

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

    public RezultatDTO getRezultatDTO() {
        return rezultatDTO;
    }

    public void setRezultatDTO(RezultatDTO rezultatDTO) {
        this.rezultatDTO = rezultatDTO;
    }

    public ProbaDTO getProbaDTO() {
        return probaDTO;
    }

    public void setProbaDTO(ProbaDTO probaDTO) {
        this.probaDTO = probaDTO;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", arbitruDTO=" + arbitruDTO +
                ", participantDTO=" + participantDTO +
                ", rezultatDTO=" + rezultatDTO +
                ", probaDTO=" + probaDTO +
                '}';
    }

    public TipProbaDTO getTipProbaDTO() {
        return tipProbaDTO;
    }

    public void setTipProbaDTO(TipProbaDTO tipProbaDTO) {
        this.tipProbaDTO = tipProbaDTO;
    }
}

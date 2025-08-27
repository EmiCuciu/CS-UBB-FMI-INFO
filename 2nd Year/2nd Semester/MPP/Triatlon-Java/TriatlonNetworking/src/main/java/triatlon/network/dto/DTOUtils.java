package triatlon.network.dto;

import triatlon.model.Arbitru;
import triatlon.model.Participant;
import triatlon.model.Rezultat;
import triatlon.model.TipProba;

import java.util.HashMap;
import java.util.Map;

public class DTOUtils {

    // Convert domain objects to DTOs

    public static ArbitruDTO getDTO(Arbitru arbitru) {
        if (arbitru == null) return null;
        return new ArbitruDTO(
                arbitru.getId(),
                arbitru.getUsername(),
                arbitru.getPassword(),
                arbitru.getFirst_name(),
                arbitru.getLast_name()
        );
    }

    public static ParticipantDTO getDTO(Participant participant) {
        if (participant == null) return null;
        ParticipantDTO dto = new ParticipantDTO(
                participant.getId(),
                participant.getFirst_name(),
                participant.getLast_name()
        );

        // Copy punctaje from participant to DTO
        for (TipProba tipProba : TipProba.values()) {
            int punctaj = participant.getPunctajProba(tipProba);
            dto.setPunctajProba(getTipProbaDTO(tipProba), punctaj);
        }

        return dto;
    }

    public static RezultatDTO getDTO(Rezultat rezultat) {
        if (rezultat == null) return null;
        return new RezultatDTO(
                rezultat.getId(),
                getDTO(rezultat.getParticipant()),
                getDTO(rezultat.getArbitru()),
                getTipProbaDTO(rezultat.getTipProba()),
                rezultat.getPunctaj()
        );
    }

    public static ProbaDTO getDTO(Integer id, TipProba tipProba, Arbitru arbitru) {
        if (tipProba == null) return null;
        return new ProbaDTO(
                id,
                getTipProbaDTO(tipProba),
                getDTO(arbitru)
        );
    }

    public static TipProbaDTO getTipProbaDTO(TipProba tipProba) {
        if (tipProba == null) return null;
        switch (tipProba) {
            case NATATIE: return TipProbaDTO.NATATIE;
            case CICLISM: return TipProbaDTO.CICLISM;
            case ALERGARE: return TipProbaDTO.ALERGARE;
            default: return null;
        }
    }

    // Convert arrays of domain objects to arrays of DTOs

    public static ArbitruDTO[] getDTO(Arbitru[] arbitri) {
        if (arbitri == null) return null;
        ArbitruDTO[] dtos = new ArbitruDTO[arbitri.length];
        for (int i = 0; i < arbitri.length; i++) {
            dtos[i] = getDTO(arbitri[i]);
        }
        return dtos;
    }

    public static RezultatDTO[] getDTO(Rezultat[] rezultate) {
        if (rezultate == null) return null;
        RezultatDTO[] dtos = new RezultatDTO[rezultate.length];
        for (int i = 0; i < rezultate.length; i++) {
            dtos[i] = getDTO(rezultate[i]);
        }
        return dtos;
    }

    // Convert DTOs to domain objects

    public static Arbitru getFromDTO(ArbitruDTO dto) {
        if (dto == null) return null;
        return new Arbitru(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getFirst_name(),
                dto.getLast_name()
        );
    }

    public static Participant getFromDTO(ParticipantDTO dto) {
        if (dto == null) return null;
        Participant participant = new Participant(
                dto.getId(),
                dto.getFirst_name(),
                dto.getLast_name()
        );

        // Copy punctaje from DTO to participant
        for (TipProbaDTO tipProbaDTO : TipProbaDTO.values()) {
            TipProba tipProba = getFromDTO(tipProbaDTO);
            if (tipProba != null) {
                int punctaj = dto.getPunctajProba(tipProbaDTO);
                participant.setPunctajProba(tipProba, punctaj);
            }
        }

        return participant;
    }

    public static Rezultat getFromDTO(RezultatDTO dto) {
        if (dto == null) return null;
        return new Rezultat(
                dto.getId(),
                getFromDTO(dto.getParticipant()),
                getFromDTO(dto.getArbitru()),
                getFromDTO(dto.getTipProba()),
                dto.getPunctaj()
        );
    }

    public static TipProba getFromDTO(TipProbaDTO tipProbaDTO) {
        if (tipProbaDTO == null) return null;
        switch (tipProbaDTO) {
            case NATATIE: return TipProba.NATATIE;
            case CICLISM: return TipProba.CICLISM;
            case ALERGARE: return TipProba.ALERGARE;
            default: return null;
        }
    }

    // Convert arrays of DTOs to arrays of domain objects

    public static Arbitru[] getFromDTO(ArbitruDTO[] dtos) {
        if (dtos == null) return null;
        Arbitru[] arbitri = new Arbitru[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            arbitri[i] = getFromDTO(dtos[i]);
        }
        return arbitri;
    }

    public static Rezultat[] getFromDTO(RezultatDTO[] dtos) {
        if (dtos == null) return null;
        Rezultat[] rezultate = new Rezultat[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            rezultate[i] = getFromDTO(dtos[i]);
        }
        return rezultate;
    }

    public static Object getFromDTO(ParticipantDTO[] participantiDTO) {
        if (participantiDTO == null) return null;
        Map<Integer, Participant> participanti = new HashMap<>();
        for (ParticipantDTO dto : participantiDTO) {
            participanti.put(dto.getId(), getFromDTO(dto));
        }
        return participanti.values();
    }
}
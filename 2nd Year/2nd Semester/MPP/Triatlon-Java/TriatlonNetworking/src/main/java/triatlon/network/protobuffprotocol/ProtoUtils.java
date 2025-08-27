package triatlon.network.protobuffprotocol;

import triatlon.model.*;

import java.util.List;

/**
 * Utility class for converting between domain model objects and Protocol Buffer messages
 */
public class ProtoUtils {

    public static TriatlonProtobufs.Arbitru toProto(Arbitru arbitru) {
        if (arbitru == null) return null;

        TriatlonProtobufs.Arbitru.Builder builder = TriatlonProtobufs.Arbitru.newBuilder()
                .setUsername(arbitru.getUsername())
                .setPassword(arbitru.getPassword());

        if (arbitru.getId() != null) {
            builder.setId(arbitru.getId());
        }

        if (arbitru.getFirst_name() != null) {
            builder.setFirstName(arbitru.getFirst_name());
        }

        if (arbitru.getLast_name() != null) {
            builder.setLastName(arbitru.getLast_name());
        }

        return builder.build();
    }

    public static Arbitru fromProto(TriatlonProtobufs.Arbitru protoArbitru) {
        if (protoArbitru == null) return null;

        return new Arbitru(
                protoArbitru.getId(),
                protoArbitru.getUsername(),
                protoArbitru.getPassword(),
                protoArbitru.getFirstName(),
                protoArbitru.getLastName()
        );
    }

    public static TriatlonProtobufs.Participant toProto(Participant participant) {
        if (participant == null) return null;

        TriatlonProtobufs.Participant.Builder builder = TriatlonProtobufs.Participant.newBuilder();

        if (participant.getId() != null) {
            builder.setId(participant.getId());
        }

        if (participant.getFirst_name() != null) {
            builder.setFirstName(participant.getFirst_name());
        }

        if (participant.getLast_name() != null) {
            builder.setLastName(participant.getLast_name());
        }

        return builder.build();
    }

    public static Participant fromProto(TriatlonProtobufs.Participant protoParticipant) {
        if (protoParticipant == null) return null;

        return new Participant(
                protoParticipant.getId(),
                protoParticipant.getFirstName(),
                protoParticipant.getLastName()
        );
    }

    public static TriatlonProtobufs.TipProba toProto(TipProba tipProba) {
        if (tipProba == null) return null;

        return TriatlonProtobufs.TipProba.newBuilder()
                .setDenumire(tipProba.getDenumire())
                .build();
    }

    public static TipProba fromProto(TriatlonProtobufs.TipProba protoTipProba) {
        if (protoTipProba == null) return null;

        switch (protoTipProba.getDenumire()) {
            case "Natatie":
                return TipProba.NATATIE;
            case "Ciclism":
                return TipProba.CICLISM;
            case "Alergare":
                return TipProba.ALERGARE;
            default:
                return null;
        }
    }

    public static TriatlonProtobufs.Proba toProto(Proba proba) {
        if (proba == null) return null;

        TriatlonProtobufs.Proba.Builder builder = TriatlonProtobufs.Proba.newBuilder();

        if (proba.getId() != null) {
            builder.setId(proba.getId());
        }

        if (proba.getTipProba() != null) {
            builder.setTipProba(toProto(proba.getTipProba()));
        }

        if (proba.getArbitru() != null) {
            builder.setArbitru(toProto(proba.getArbitru()));
        }

        return builder.build();
    }

    public static Proba fromProto(TriatlonProtobufs.Proba protoProba) {
        if (protoProba == null) return null;

        return new Proba(
                protoProba.getId(),
                fromProto(protoProba.getTipProba()),
                fromProto(protoProba.getArbitru())
        );
    }

    public static TriatlonProtobufs.Rezultat toProto(Rezultat rezultat) {
        if (rezultat == null) return null;

        TriatlonProtobufs.Rezultat.Builder builder = TriatlonProtobufs.Rezultat.newBuilder()
                .setPunctaj(rezultat.getPunctaj());

        if (rezultat.getId() != null) {
            builder.setId(rezultat.getId());
        }

        if (rezultat.getParticipant() != null) {
            builder.setParticipant(toProto(rezultat.getParticipant()));
        }

        if (rezultat.getArbitru() != null) {
            builder.setArbitru(toProto(rezultat.getArbitru()));
        }

        if (rezultat.getTipProba() != null) {
            builder.setTipProba(toProto(rezultat.getTipProba()));
        }

        return builder.build();
    }

    public static Rezultat fromProto(TriatlonProtobufs.Rezultat protoRezultat) {
        if (protoRezultat == null) return null;

        return new Rezultat(
                protoRezultat.getId(),
                fromProto(protoRezultat.getParticipant()),
                fromProto(protoRezultat.getArbitru()),
                fromProto(protoRezultat.getTipProba()),
                protoRezultat.getPunctaj()
        );
    }

    // Request creation methods
    public static TriatlonProtobufs.Request createLoginRequest(Arbitru arbitru) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.LOGIN)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static TriatlonProtobufs.Request createLogoutRequest(Arbitru arbitru) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.LOGOUT)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static TriatlonProtobufs.Request createAddResultRequest(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.ADD_RESULT)
                .setParticipant(toProto(participant))
                .setArbitru(toProto(arbitru))
                .setTipProba(toProto(tipProba))
                .setPunctaj(punctaj)
                .build();
    }

    public static TriatlonProtobufs.Request createGetResultsForProbaRequest(TipProba tipProba) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.GET_RESULTS_FOR_PROBA)
                .setTipProba(toProto(tipProba))
                .build();
    }

    public static TriatlonProtobufs.Request createGetAllParticipantsRequest() {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.GET_ALL_PARTICIPANTS)
                .build();
    }

    public static TriatlonProtobufs.Request createCalculateTotalScoreRequest(Participant participant) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.CALCULATE_TOTAL_SCORE)
                .setParticipant(toProto(participant))
                .build();
    }

    public static TriatlonProtobufs.Request createGetAllProbeRequest() {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.GET_ALL_PROBE)
                .build();
    }

    public static TriatlonProtobufs.Request createGetProbaForArbitruRequest(Arbitru arbitru) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.GET_PROBA_FOR_ARBITRU)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static TriatlonProtobufs.Request createFindArbitruRequest(String username, String password) {
        TriatlonProtobufs.Arbitru arbitruCredentials = TriatlonProtobufs.Arbitru.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.FIND_ARBITRU)
                .setArbitru(arbitruCredentials)
                .build();
    }

    // Response creation methods
    public static TriatlonProtobufs.Response createOkResponse() {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.OK)
                .build();
    }

    public static TriatlonProtobufs.Response createErrorResponse(String text) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.ERROR)
                .setError(text)
                .build();
    }

    public static TriatlonProtobufs.Response createArbitruLoggedInResponse(Arbitru arbitru) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.REFREE_LOGGED_IN)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static TriatlonProtobufs.Response createArbitruLoggedOutResponse(Arbitru arbitru) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.REFREE_LOGGED_OUT)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static TriatlonProtobufs.Response createRezultatAddedResponse(Rezultat rezultat) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.RESULTAT_ADDED)
                .setRezultat(toProto(rezultat))
                .build();
    }

    public static TriatlonProtobufs.Response createGetAllParticipantsResponse(List<Participant> participants) {
        TriatlonProtobufs.Response.Builder response = TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.GET_ALL_PARTICIPANTS);

        for (Participant participant : participants) {
            response.addParticipants(toProto(participant));
        }

        return response.build();
    }

    public static TriatlonProtobufs.Response createGetResultsForProbaResponse(List<Rezultat> rezultate) {
        TriatlonProtobufs.Response.Builder response = TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.GET_RESULTS_FOR_PROBA);

        for (Rezultat rezultat : rezultate) {
            response.addRezultate(toProto(rezultat));
        }

        return response.build();
    }

    public static TriatlonProtobufs.Response createGetAllProbeResponse(List<Proba> probe) {
        TriatlonProtobufs.Response.Builder response = TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.GET_ALL_PROBE);

        for (Proba proba : probe) {
            response.addProbe(toProto(proba));
        }

        return response.build();
    }

    public static TriatlonProtobufs.Response createCalculateTotalScoreResponse(int totalScore) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.OK)
                .setTotalScore(totalScore)
                .build();
    }

    public static TriatlonProtobufs.Request createEmptyRequest(TriatlonProtobufs.Request.Type type) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(type)
                .build();
    }

    public static Arbitru getArbitru(TriatlonProtobufs.Arbitru arbitru) {
        return fromProto(arbitru);
    }

    public static Rezultat getRezultat(TriatlonProtobufs.Rezultat rezultat) {
        return fromProto(rezultat);
    }

    public static TriatlonProtobufs.Request createRegisterRequest(Arbitru arbitru) {
        return TriatlonProtobufs.Request.newBuilder()
                .setType(TriatlonProtobufs.Request.Type.REGISTER)
                .setArbitru(toProto(arbitru))
                .build();
    }

    public static Participant getParticipant(TriatlonProtobufs.Participant participantProto) {
        return fromProto(participantProto);
    }

    public static Proba getProba(TriatlonProtobufs.Proba probaProto) {
        return fromProto(probaProto);
    }

    public static TriatlonProtobufs.Arbitru createArbitru(Arbitru arbitru) {
        return TriatlonProtobufs.Arbitru.newBuilder()
                .setId(arbitru.getId())
                .setUsername(arbitru.getUsername())
                .setPassword(arbitru.getPassword())
                .setFirstName(arbitru.getFirst_name())
                .setLastName(arbitru.getLast_name())
                .build();
    }

    public static TipProba getTipProba(TriatlonProtobufs.TipProba tipProba) {
        return fromProto(tipProba);
    }

    public static TriatlonProtobufs.Response createGetProbaForArbitruResponse(TipProba tipProba) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.GET_PROBA_FOR_ARBITRU)
                .setTipProba(toProto(tipProba))
                .build();
    }

    public static TriatlonProtobufs.Response createFindArbitruResponse(Arbitru foundArbitru) {
        return TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.FIND_ARBITRU)
                .setArbitru(toProto(foundArbitru))
                .build();
    }

    public static TriatlonProtobufs.Response createGetAllResultsResponse(List<Rezultat> allResults) {
        TriatlonProtobufs.Response.Builder response = TriatlonProtobufs.Response.newBuilder()
                .setType(TriatlonProtobufs.Response.Type.GET_ALL_RESULTS);

        for (Rezultat rezultat : allResults) {
            response.addRezultate(toProto(rezultat));
        }

        return response.build();
    }
}
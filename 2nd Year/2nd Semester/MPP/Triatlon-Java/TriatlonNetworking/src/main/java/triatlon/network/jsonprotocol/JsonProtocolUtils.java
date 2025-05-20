package triatlon.network.jsonprotocol;

import triatlon.model.Arbitru;
import triatlon.model.Rezultat;
import triatlon.network.dto.ArbitruDTO;
import triatlon.network.dto.DTOUtils;
import triatlon.network.dto.RezultatDTO;


public class JsonProtocolUtils {


    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Request createLoginRequest(Arbitru user) {
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setArbitruDTO(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createLogoutRequest(Arbitru user) {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setArbitruDTO(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createAddResultRequest(Arbitru user, Rezultat rezultat) {
        Request req = new Request();
        req.setType(RequestType.ADD_RESULT);
        req.setArbitruDTO(DTOUtils.getDTO(user));
        req.setRezultatDTO(DTOUtils.getDTO(rezultat));
        return req;
    }


    public static Response createArbitruLoggedInResponse(Arbitru arbitru) {
        Response resp = new Response();
        resp.setType(ResponseType.REFREE_LOGGED_IN);
        resp.setArbitruDTO(DTOUtils.getDTO(arbitru));
        return resp;
    }

    public static Response createArbitruLoggedOutResponse(Arbitru arbitru) {
        Response resp = new Response();
        resp.setType(ResponseType.REFREE_LOGGED_OUT);
        resp.setArbitruDTO(DTOUtils.getDTO(arbitru));
        return resp;
    }

    public static Response createRezultatAddedResponse(Rezultat rezultat) {
        Response resp = new Response();
        resp.setType(ResponseType.REZULTAT_ADDED);
        resp.setRezultatDTO(DTOUtils.getDTO(rezultat));
        return resp;
    }


    public static Response createGetResultsForProbaResponse(RezultatDTO[] rezultatDTOs) {
        Response resp = new Response();
        resp.setType(ResponseType.GET_RESULTS_FOR_PROBA);
        resp.setRezultateDTO(rezultatDTOs);
        return resp;
    }

    public static Request createFindArbitruRequest(String username, String password) {
        Request req = new Request();
        req.setType(RequestType.FIND_ARBITRU);

        // Create a temporary arbitru with just username and password
        ArbitruDTO arbitruDTO = new ArbitruDTO(null, username, password, null, null);
        req.setArbitruDTO(arbitruDTO);
        return req;
    }
}

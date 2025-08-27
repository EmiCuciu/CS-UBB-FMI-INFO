package triatlon.network.jsonprotocol;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.model.*;
import triatlon.network.dto.*;
import triatlon.services.ITriatlonObserver;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static triatlon.network.jsonprotocol.JsonProtocolUtils.createErrorResponse;

public class TriatlonClientJsonWorker implements Runnable, ITriatlonObserver {
    private final ITriatlonServices server;
    private final Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private final Gson gsonFormatter;
    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger(TriatlonClientJsonWorker.class);

    public TriatlonClientJsonWorker(ITriatlonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            logger.error("Error creating worker: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }

    private void sendResponse(Response response) throws IOException {
        String responseLine = gsonFormatter.toJson(response);
        logger.debug("sending response " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    // ITriatlonObserver methods
    @Override
    public void arbitruLoggedIn(Arbitru arbitru) throws TriatlonException {
        Response resp = JsonProtocolUtils.createArbitruLoggedInResponse(arbitru);
        try {
            sendResponse(resp);
        } catch (Exception e) {
            throw new TriatlonException("Error sending arbitru logged in notification: " + e.getMessage());
        }
    }

    @Override
    public void arbitruLoggedOut(Arbitru arbitru) throws TriatlonException {
        Response resp = JsonProtocolUtils.createArbitruLoggedOutResponse(arbitru);
        try {
            sendResponse(resp);
        } catch (Exception e) {
            throw new TriatlonException("Error sending arbitru logged out notification: " + e.getMessage());
        }
    }

    @Override
    public void rezultatAdded(Rezultat rezultat) throws TriatlonException {
        Response resp = JsonProtocolUtils.createRezultatAddedResponse(rezultat);
        try {
            sendResponse(resp);
        } catch (Exception e) {
            throw new TriatlonException("Error sending rezultat added notification: " + e.getMessage());
        }
    }


    private static final Response okResponse = JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            logger.debug("Login request ...{}" , request.getArbitruDTO());
            ArbitruDTO arbitruDTO = request.getArbitruDTO();
            Arbitru arbitru = DTOUtils.getFromDTO(arbitruDTO);
            try {
                server.login(arbitru, this);
                return okResponse;
            } catch (TriatlonException e) {
                connected = false;
                return createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.LOGOUT) {
            logger.debug("Logout request ...{}" , request.getArbitruDTO());
            ArbitruDTO arbitruDTO = request.getArbitruDTO();
            Arbitru arbitru = DTOUtils.getFromDTO(arbitruDTO);
            try {
                server.logout(arbitru, this);
                connected = false;
                return okResponse;
            } catch (TriatlonException e) {
                connected = false;
                return createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.ADD_RESULT) {
            logger.debug("Add rezultat request ...{}" , request.getRezultatDTO());
            RezultatDTO rezultatDTO = request.getRezultatDTO();
            Rezultat rezultat = DTOUtils.getFromDTO(rezultatDTO);
            try {
                server.addRezultat(rezultat.getParticipant(), rezultat.getArbitru(), rezultat.getTipProba(), rezultat.getPunctaj());
                return okResponse;
            } catch (TriatlonException e) {
                return createErrorResponse(e.getMessage());
            }
        }

//        if (request.getType() == RequestType.GET_ALL_RESULTS) {
//            logger.debug("Get all results request ...{}" , request.getArbitruDTO());
//            List<Rezultat> rezultate = server.getAllResults();
//            RezultatDTO[] rezultatDTOs = DTOUtils.getDTO(rezultate.toArray(new Rezultat[0]));
//            return JsonProtocolUtils.createGetAllResultsResponse(rezultatDTOs);
//        }

        if (request.getType() == RequestType.GET_RESULTS_FOR_PROBA) {
            logger.debug("Get results for proba request ...{}", request.getTipProbaDTO());
            try {
                // Get the TipProba from the request
                TipProba tipProba = DTOUtils.getFromDTO(request.getTipProbaDTO());
                List<Rezultat> rezultate = server.getResultateForProba(tipProba);
                RezultatDTO[] rezultatDTOs = DTOUtils.getDTO(rezultate.toArray(new Rezultat[0]));
                return JsonProtocolUtils.createGetResultsForProbaResponse(rezultatDTOs);
            } catch (TriatlonException e) {
                return createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_PROBA_FOR_ARBITRU) {
            ArbitruDTO arbitruDTO = request.getArbitruDTO();
            Arbitru arbitru = DTOUtils.getFromDTO(arbitruDTO);
            Response resp = new Response();
            resp.setType(ResponseType.OK);

            try {
                TipProba tipProba = server.getProbaForArbitru(arbitru);
                if (tipProba != null) {
                    // Convert to DTO
                    TipProbaDTO tipProbaDTO = DTOUtils.getTipProbaDTO(tipProba);
                    // Create a ProbaDTO with the TipProbaDTO
                    ProbaDTO probaDTO = new ProbaDTO(null, tipProbaDTO, arbitruDTO);
                    resp.setProbaDTO(probaDTO);
                } else {
                    // Set an explicit null ProbaDTO to indicate no match
                    resp.setProbaDTO(null);
                }
            } catch (TriatlonException e) {
                resp = JsonProtocolUtils.createErrorResponse(e.getMessage());
            }

            return resp;
        }

        if (request.getType() == RequestType.GET_ALL_PARTICIPANTS) {
            Response resp = new Response();
            resp.setType(ResponseType.GET_ALL_PARTICIPANTS);
            try {
                List<Participant> participants = server.getAllParticipants();
                ParticipantDTO[] participantDTOs = participants.stream()
                        .map(DTOUtils::getDTO)
                        .toArray(ParticipantDTO[]::new);
                resp.setParticipanti(participantDTOs);
            } catch (TriatlonException e) {
                resp = JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
            return resp;
        }

        if (request.getType() == RequestType.CALCULATE_TOTAL_SCORE) {
            Response resp = new Response();
            resp.setType(ResponseType.OK);
            try {
                Participant participant = DTOUtils.getFromDTO(request.getParticipantDTO());
                int totalScore = server.calculateTotalScore(participant);
                // Using errorMessage field to transfer the score as a string
                resp.setErrorMessage(String.valueOf(totalScore));
                return resp;
            } catch (TriatlonException e) {
                return createErrorResponse("Error calculating score: " + e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_ALL_PROBE) {
            Response resp = new Response();
            resp.setType(ResponseType.OK);
            try {
                List<Proba> probe = server.getAllProbe();
                ProbaDTO[] probeDTOs = new ProbaDTO[probe.size()];
                for (int i = 0; i < probe.size(); i++) {
                    Proba p = probe.get(i);
                    probeDTOs[i] = DTOUtils.getDTO(p.getId(), p.getTipProba(), p.getArbitru());
                }
                resp.setProbe(probeDTOs);
                return resp;
            } catch (TriatlonException e) {
                return createErrorResponse("Error getting probe: " + e.getMessage());
            }
        }

        if (request.getType() == RequestType.FIND_ARBITRU) {
            ArbitruDTO arbitruDTO = request.getArbitruDTO();
            Arbitru arbitru = server.findArbitruByUsernameAndPassword(
                    arbitruDTO.getUsername(),
                    arbitruDTO.getPassword()
            );

            Response resp = new Response();
            if (arbitru != null) {
                resp.setType(ResponseType.OK);
                resp.setArbitruDTO(DTOUtils.getDTO(arbitru));
            } else {
                resp.setType(ResponseType.ERROR);
                resp.setErrorMessage("Invalid credentials");
            }
            return resp;
        }

        return response;
    }
}

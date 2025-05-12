package triatlon.network.protobuffprotocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.model.*;
import triatlon.services.ITriatlonObserver;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ProtoTriatlonWorker implements Runnable, ITriatlonObserver {
    private final ITriatlonServices server;
    private final Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger(ProtoTriatlonWorker.class);

    public ProtoTriatlonWorker(ITriatlonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = connection.getOutputStream();
            input = connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            logger.error("Error creating worker: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                // Read the request
                TriatlonProtobufs.Request request = TriatlonProtobufs.Request.parseDelimitedFrom(input);
                if (request == null) {
                    break;
                }

                logger.debug("Received request: {}", request);

                // Process request and get response
                TriatlonProtobufs.Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error("Error processing request: {}", e.getMessage());
                connected = false;
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error closing connection: {}", e.getMessage());
        }
    }

    private void sendResponse(TriatlonProtobufs.Response response) throws IOException {
        logger.debug("Sending response: {}", response);
        synchronized (output) {
            response.writeDelimitedTo(output);
            output.flush();
        }
    }

    // ITriatlonObserver methods
    @Override
    public void arbitruLoggedIn(Arbitru arbitru) throws TriatlonException {
        TriatlonProtobufs.Response response = ProtoUtils.createArbitruLoggedInResponse(arbitru);
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new TriatlonException("Error sending arbitru logged in notification: " + e.getMessage());
        }
    }

    @Override
    public void arbitruLoggedOut(Arbitru arbitru) throws TriatlonException {
        TriatlonProtobufs.Response response = ProtoUtils.createArbitruLoggedOutResponse(arbitru);
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new TriatlonException("Error sending arbitru logged out notification: " + e.getMessage());
        }
    }

    @Override
    public void rezultatAdded(Rezultat rezultat) throws TriatlonException {
        TriatlonProtobufs.Response response = ProtoUtils.createRezultatAddedResponse(rezultat);
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new TriatlonException("Error sending rezultat added notification: " + e.getMessage());
        }
    }

    private static final TriatlonProtobufs.Response okResponse = ProtoUtils.createOkResponse();

    private TriatlonProtobufs.Response handleRequest(TriatlonProtobufs.Request request) {
        switch (request.getType()) {
            case LOGIN:
                logger.debug("Login request: {}", request.getArbitru());
                Arbitru loginArbitru = ProtoUtils.getArbitru(request.getArbitru());
                try {
                    server.login(loginArbitru, this);
                    return okResponse;
                } catch (TriatlonException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case LOGOUT:
                logger.debug("Logout request: {}", request.getArbitru());
                Arbitru logoutArbitru = ProtoUtils.getArbitru(request.getArbitru());
                try {
                    server.logout(logoutArbitru, this);
                    connected = false;
                    return okResponse;
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case REGISTER:
                logger.debug("Register request: {}", request.getArbitru());
                Arbitru registerArbitru = ProtoUtils.getArbitru(request.getArbitru());
                try {
                    boolean success = server.register(
                            registerArbitru.getUsername(),
                            registerArbitru.getPassword(),
                            registerArbitru.getFirst_name(),
                            registerArbitru.getLast_name()
                    );
                    if (success) {
                        return okResponse;
                    } else {
                        return ProtoUtils.createErrorResponse("Registration failed - username already exists");
                    }
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case ADD_RESULT:
                logger.debug("Add result request");
                try {
                    Participant participant = ProtoUtils.getParticipant(request.getParticipant());
                    Arbitru arbitru = ProtoUtils.getArbitru(request.getArbitru());

                    TipProba tipProba = server.getProbaForArbitru(arbitru);
                    int punctaj = request.getPunctaj();

                    logger.debug("participant: {}, arbitru: {}, tipProba: {}, punctaj: {}", participant, arbitru, tipProba, punctaj);

                    if (tipProba == null) {
                        return ProtoUtils.createErrorResponse("TipProba is null");
                    }

                    server.addRezultat(participant, arbitru, tipProba, punctaj);
                    return okResponse;
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case GET_RESULTS_FOR_PROBA:
                logger.debug("Get results for proba request");
                try {
                    TipProba tipProba = ProtoUtils.getTipProba(request.getTipProba());
                    List<Rezultat> rezultate = server.getResultateForProba(tipProba);
                    return ProtoUtils.createGetResultsForProbaResponse(rezultate);
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case GET_ALL_PARTICIPANTS:
                logger.debug("Get all participants request");
                try {
                    List<Participant> participants = server.getAllParticipants();
                    return ProtoUtils.createGetAllParticipantsResponse(participants);
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case CALCULATE_TOTAL_SCORE:
                logger.debug("Calculate total score request");
                try {
                    Participant participant = ProtoUtils.getParticipant(request.getParticipant());
                    int totalScore = server.calculateTotalScore(participant);
                    return ProtoUtils.createCalculateTotalScoreResponse(totalScore);
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case GET_ALL_PROBE:
                logger.debug("Get all probe request");
                try {
                    List<Proba> probe = server.getAllProbe();
                    return ProtoUtils.createGetAllProbeResponse(probe);
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case GET_PROBA_FOR_ARBITRU:
                logger.debug("Get proba for arbitru request");
                try {
                    Arbitru arbitru = ProtoUtils.getArbitru(request.getArbitru());
                    TipProba tipProba = server.getProbaForArbitru(arbitru);
                    return ProtoUtils.createGetProbaForArbitruResponse(tipProba);
                } catch (TriatlonException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }

            case FIND_ARBITRU:
                logger.debug("Find arbitru request");
                TriatlonProtobufs.Arbitru arbitruDTO = request.getArbitru();
                logger.debug("Username: {}, Password: {}", arbitruDTO.getUsername(), arbitruDTO.getPassword());

                Arbitru foundArbitru = server.findArbitruByUsernameAndPassword(
                        arbitruDTO.getUsername(),
                        arbitruDTO.getPassword());

                if (foundArbitru != null) {
                    return ProtoUtils.createFindArbitruResponse(foundArbitru);
                } else {
                    return ProtoUtils.createErrorResponse("Invalid credentials");
                }

            case GET_ALL_RESULTS:
                logger.debug("Get all results request");
                List<Rezultat> allResults = server.getAllResults();
                return ProtoUtils.createGetAllResultsResponse(allResults);

            default:
                return ProtoUtils.createErrorResponse("Invalid request type");
        }
    }
}
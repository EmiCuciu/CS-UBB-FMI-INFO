package triatlon.network.jsonprotocol;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.model.*;
import triatlon.network.dto.DTOUtils;
import triatlon.network.dto.ParticipantDTO;
import triatlon.network.dto.ProbaDTO;
import triatlon.network.dto.RezultatDTO;
import triatlon.services.ITriatlonObserver;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TriatlonServicesJsonProxy implements ITriatlonServices {
    private String host;
    private int port;

    private ITriatlonObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Socket connection;

    private BlockingQueue<Response> responses;
    private volatile boolean finished;
    private final Gson gson;

    private static final Logger logger = LogManager.getLogger(TriatlonServicesJsonProxy.class);

    public TriatlonServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
        gson = new Gson();
    }

    private void initializeConnection() throws TriatlonException {
        try {
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            logger.error("Error connecting to server: " + e.getMessage());
            throw new TriatlonException("Error connecting to server: " + e.getMessage());
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(Request request) throws TriatlonException {
        try {
            String requestStr = gson.toJson(request);
            logger.debug("Sending request: {}", requestStr);
            output.println(requestStr);
            output.flush();
        } catch (Exception e) {
            logger.error("Error sending request: " + e.getMessage());
            throw new TriatlonException("Error sending request: " + e.getMessage());
        }
    }

    private Response readResponse() throws TriatlonException {
        try {
            Response response = responses.take();
            logger.debug("Response received: {}", response);
            if (response.getType() == ResponseType.ERROR) {
                String errorMsg = response.getErrorMessage();
                logger.error("Error received from server: {}", errorMsg);
                throw new TriatlonException(errorMsg);
            }
            return response;
        } catch (InterruptedException e) {
            logger.error("Error reading response: " + e.getMessage());
            throw new TriatlonException("Error reading response: " + e.getMessage());
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.REFREE_LOGGED_IN ||
                response.getType() == ResponseType.REFREE_LOGGED_OUT ||
                response.getType() == ResponseType.REZULTAT_ADDED;
    }

    private void handleUpdate(Response response) {
        if (client == null) return;
        try {
            switch (response.getType()) {
                case REFREE_LOGGED_IN:
                    Arbitru arbitruLoggedIn = DTOUtils.getFromDTO(response.getArbitruDTO());
                    client.arbitruLoggedIn(arbitruLoggedIn);
                    break;
                case REFREE_LOGGED_OUT:
                    Arbitru arbitruLoggedOut = DTOUtils.getFromDTO(response.getArbitruDTO());
                    client.arbitruLoggedOut(arbitruLoggedOut);
                    break;
                case REZULTAT_ADDED:
                    Rezultat rezultatAdded = DTOUtils.getFromDTO(response.getRezultatDTO());
                    client.rezultatAdded(rezultatAdded);
                    break;
            }
        } catch (TriatlonException e) {
            logger.error("Error handling update: " + e.getMessage());
        }
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    if (responseLine == null) {
                        logger.error("Server closed connection");
                        finished = true;
                        break;
                    }
                    logger.debug("Response received: {}", responseLine);
                    Response response = gson.fromJson(responseLine, Response.class);
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        responses.add(response);
                    }
                } catch (IOException e) {
                    logger.error("Error reading response: " + e.getMessage());
                    finished = true;
                }
            }
        }
    }

    // ITriatlonServices implementation

    @Override
    public void login(Arbitru arbitru, ITriatlonObserver iTriatlonObserver) throws TriatlonException {
        initializeConnection();
        this.client = iTriatlonObserver;

        // Ensure we're sending the complete arbitru object
        Request req = JsonProtocolUtils.createLoginRequest(arbitru);
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() != ResponseType.OK) {
            closeConnection();
            throw new TriatlonException("Login failed");
        }
    }

    @Override
    public boolean register(String username, String password, String firstName, String lastName) throws TriatlonException {
        if (connection == null) {
            initializeConnection();
        }
        Arbitru arbitru = new Arbitru(null, username, password, firstName, lastName);
        Request req = new Request();
        req.setType(RequestType.REGISTER);
        req.setArbitruDTO(DTOUtils.getDTO(arbitru));
        sendRequest(req);
        Response response = readResponse();
        return response.getType() == ResponseType.OK;
    }

    @Override
    public void logout(Arbitru arbitru, ITriatlonObserver iTriatlonObserver) throws TriatlonException {
        Request req = JsonProtocolUtils.createLogoutRequest(arbitru);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        client = null;
    }

    @Override
    public void addRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) throws TriatlonException {
        Rezultat rezultat = new Rezultat(null, participant, arbitru, tipProba, punctaj);
        Request req = JsonProtocolUtils.createAddResultRequest(arbitru, rezultat);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() != ResponseType.OK) {
            throw new TriatlonException("Failed to add result");
        }
    }

    @Override
    public List<Rezultat> getResultateForProba(TipProba proba) throws TriatlonException {
        Request req = new Request();
        req.setType(RequestType.GET_RESULTS_FOR_PROBA);
        req.setTipProbaDTO(DTOUtils.getTipProbaDTO(proba));
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.GET_RESULTS_FOR_PROBA) {
            return Arrays.asList(response.getRezultate());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Rezultat> getAllResults() {
        try {
            Request req = new Request();
            req.setType(RequestType.GET_ALL_RESULTS);
            sendRequest(req);
            Response response = readResponse();

            if (response.getType() == ResponseType.GET_ALL_RESULTS) {
                RezultatDTO[] rezultatDTOs = response.getRezultateDTO();
                List<Rezultat> rezultate = new ArrayList<>();
                for (RezultatDTO dto : rezultatDTOs) {
                    rezultate.add(DTOUtils.getFromDTO(dto));
                }
                return rezultate;
            }
        } catch (TriatlonException e) {
            logger.error("Error getting all results: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Participant> getAllParticipants() throws TriatlonException {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_PARTICIPANTS);
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.GET_ALL_PARTICIPANTS) {
            ParticipantDTO[] participantDTOs = response.getParticipanti();
            List<Participant> participants = new ArrayList<>();
            for (ParticipantDTO dto : participantDTOs) {
                participants.add(DTOUtils.getFromDTO(dto));
            }
            return participants;
        }
        return new ArrayList<>();
    }

    @Override
    public int calculateTotalScore(Participant participant) throws TriatlonException {
        Request req = new Request();
        req.setType(RequestType.CALCULATE_TOTAL_SCORE);
        req.setParticipantDTO(DTOUtils.getDTO(participant));
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.OK) {
            return Integer.parseInt(response.getErrorMessage()); // using errorMessage field to store the score
        }
        return 0;
    }

    @Override
    public List<Proba> getAllProbe() throws TriatlonException {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_PROBE);
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.OK) {
            ProbaDTO[] probaDTOs = response.getProbe();
            List<Proba> probe = new ArrayList<>();
            if (probaDTOs != null) {
                for (ProbaDTO dto : probaDTOs) {
                    Arbitru arbitru = DTOUtils.getFromDTO(dto.getArbitru());
                    TipProba tipProba = DTOUtils.getFromDTO(dto.getTipProba());
                    probe.add(new Proba(dto.getId(), tipProba, arbitru));
                }
            }
            return probe;
        }
        return new ArrayList<>();
    }

    @Override
    public TipProba getProbaForArbitru(Arbitru arbitru) throws TriatlonException {
        Request req = new Request();
        req.setType(RequestType.GET_PROBA_FOR_ARBITRU);
        req.setArbitruDTO(DTOUtils.getDTO(arbitru));
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.OK) {
            return DTOUtils.getFromDTO(response.getTipProba());
        }
        return null;
    }

    @Override
    public Arbitru findArbitruByUsernameAndPassword(String username, String password) {
        try {
            initializeConnection();

            // Use the new utility method
            Request req = JsonProtocolUtils.createFindArbitruRequest(username, password);
            sendRequest(req);
            Response response = readResponse();

            if (response.getType() == ResponseType.OK) {
                return DTOUtils.getFromDTO(response.getArbitruDTO());
            }
            return null;
        } catch (TriatlonException e) {
            logger.error("Error finding arbitru: {}", e.getMessage());
            return null;
        }
    }
}
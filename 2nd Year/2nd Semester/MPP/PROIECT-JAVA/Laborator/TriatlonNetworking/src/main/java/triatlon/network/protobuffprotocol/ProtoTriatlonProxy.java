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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoTriatlonProxy implements ITriatlonServices {
    private String host;
    private int port;
    private ITriatlonObserver client;
    private Socket connection;
    private InputStream input;
    private OutputStream output;
    private BlockingQueue<TriatlonProtobufs.Response> responses;
    private volatile boolean finished;
    private static final Logger logger = LogManager.getLogger(ProtoTriatlonProxy.class);

    public ProtoTriatlonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() throws TriatlonException {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
            input = connection.getInputStream();
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
            client = null;
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(TriatlonProtobufs.Request request) throws TriatlonException {
        try {
            logger.debug("Sending request: {}", request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            logger.error("Error sending request: " + e.getMessage());
            throw new TriatlonException("Error sending request: " + e.getMessage());
        }
    }

    private TriatlonProtobufs.Response readResponse() throws TriatlonException {
        try {
            TriatlonProtobufs.Response response = responses.take();
            logger.debug("Response received: {}", response);
            if (response.getType() == TriatlonProtobufs.Response.Type.ERROR) {
                String errorMsg = response.getError();
                logger.error("Error received from server: {}", errorMsg);
                throw new TriatlonException(errorMsg);
            }
            return response;
        } catch (InterruptedException e) {
            logger.error("Error reading response: " + e.getMessage());
            throw new TriatlonException("Error reading response: " + e.getMessage());
        }
    }

    private boolean isUpdate(TriatlonProtobufs.Response response) {
        return response.getType() == TriatlonProtobufs.Response.Type.REFREE_LOGGED_IN ||
                response.getType() == TriatlonProtobufs.Response.Type.REFREE_LOGGED_OUT ||
                response.getType() == TriatlonProtobufs.Response.Type.RESULTAT_ADDED;
    }

    private void handleUpdate(TriatlonProtobufs.Response response) {
        if (client == null) return;
        try {
            switch (response.getType()) {
                case REFREE_LOGGED_IN:
                    Arbitru arbitruLoggedIn = ProtoUtils.getArbitru(response.getArbitru());
                    client.arbitruLoggedIn(arbitruLoggedIn);
                    break;
                case REFREE_LOGGED_OUT:
                    Arbitru arbitruLoggedOut = ProtoUtils.getArbitru(response.getArbitru());
                    client.arbitruLoggedOut(arbitruLoggedOut);
                    break;
                case RESULTAT_ADDED:
                    Rezultat rezultatAdded = ProtoUtils.getRezultat(response.getRezultat());
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
                    TriatlonProtobufs.Response response = TriatlonProtobufs.Response.parseDelimitedFrom(input);
                    if (response == null) {
                        logger.error("Server closed connection");
                        finished = true;
                        break;
                    }
                    logger.debug("Response received: {}", response);
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
    public void login(Arbitru arbitru, ITriatlonObserver observer) throws TriatlonException {
        initializeConnection();
        this.client = observer;

        TriatlonProtobufs.Request request = ProtoUtils.createLoginRequest(arbitru);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() != TriatlonProtobufs.Response.Type.OK) {
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
        TriatlonProtobufs.Request request = ProtoUtils.createRegisterRequest(arbitru);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        return response.getType() == TriatlonProtobufs.Response.Type.OK;
    }

    @Override
    public void logout(Arbitru arbitru, ITriatlonObserver observer) throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createLogoutRequest(arbitru);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();
        closeConnection();
    }

    @Override
    public void addRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj)
            throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createAddResultRequest(
                participant, arbitru, tipProba, punctaj);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() != TriatlonProtobufs.Response.Type.OK) {
            throw new TriatlonException("Failed to add result");
        }
    }

    @Override
    public List<Rezultat> getResultateForProba(TipProba proba) throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createGetResultsForProbaRequest(proba);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() == TriatlonProtobufs.Response.Type.GET_RESULTS_FOR_PROBA) {
            List<Rezultat> rezultate = new ArrayList<>();
            for (TriatlonProtobufs.Rezultat rezultatProto : response.getRezultateList()) {
                rezultate.add(ProtoUtils.getRezultat(rezultatProto));
            }
            return rezultate;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Rezultat> getAllResults() {
        try {
            TriatlonProtobufs.Request request = ProtoUtils.createEmptyRequest(
                    TriatlonProtobufs.Request.Type.GET_ALL_RESULTS);
            sendRequest(request);
            TriatlonProtobufs.Response response = readResponse();

            if (response.getType() == TriatlonProtobufs.Response.Type.GET_ALL_RESULTS) {
                List<Rezultat> rezultate = new ArrayList<>();
                for (TriatlonProtobufs.Rezultat rezultatProto : response.getRezultateList()) {
                    rezultate.add(ProtoUtils.getRezultat(rezultatProto));
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
        TriatlonProtobufs.Request request = ProtoUtils.createEmptyRequest(
                TriatlonProtobufs.Request.Type.GET_ALL_PARTICIPANTS);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() == TriatlonProtobufs.Response.Type.GET_ALL_PARTICIPANTS) {
            List<Participant> participants = new ArrayList<>();
            for (TriatlonProtobufs.Participant participantProto : response.getParticipantsList()) {
                participants.add(ProtoUtils.getParticipant(participantProto));
            }
            return participants;
        }
        return new ArrayList<>();
    }

    @Override
    public int calculateTotalScore(Participant participant) throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createCalculateTotalScoreRequest(participant);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() == TriatlonProtobufs.Response.Type.OK) {
            return response.getTotalScore();
        }
        return 0;
    }

    @Override
    public List<Proba> getAllProbe() throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createEmptyRequest(
                TriatlonProtobufs.Request.Type.GET_ALL_PROBE);
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() == TriatlonProtobufs.Response.Type.OK ||
                response.getType() == TriatlonProtobufs.Response.Type.GET_ALL_PROBE) {
            List<Proba> probe = new ArrayList<>();
            for (TriatlonProtobufs.Proba probaProto : response.getProbeList()) {
                probe.add(ProtoUtils.getProba(probaProto));
            }
            return probe;
        }
        return new ArrayList<>();
    }

    @Override
    public TipProba getProbaForArbitru(Arbitru arbitru) throws TriatlonException {
        TriatlonProtobufs.Request request = ProtoUtils.createEmptyRequest(
                TriatlonProtobufs.Request.Type.GET_PROBA_FOR_ARBITRU);
        request = request.toBuilder()
                .setArbitru(ProtoUtils.createArbitru(arbitru))
                .build();
        sendRequest(request);
        TriatlonProtobufs.Response response = readResponse();

        if (response.getType() == TriatlonProtobufs.Response.Type.OK ||
                response.getType() == TriatlonProtobufs.Response.Type.GET_PROBA_FOR_ARBITRU) {
            if (response.hasTipProba()) {
                return ProtoUtils.getTipProba(response.getTipProba());
            }
        }
        return null;
    }

    @Override
    public Arbitru findArbitruByUsernameAndPassword(String username, String password) {
        try {
            initializeConnection();

            TriatlonProtobufs.Request request = ProtoUtils.createFindArbitruRequest(username, password);
            sendRequest(request);
            TriatlonProtobufs.Response response = readResponse();

            if (response.getType() == TriatlonProtobufs.Response.Type.OK ||
                    response.getType() == TriatlonProtobufs.Response.Type.FIND_ARBITRU) {
                if (response.hasArbitru()) {
                    return ProtoUtils.getArbitru(response.getArbitru());
                }
            }
            return null;
        } catch (TriatlonException e) {
            logger.error("Error finding arbitru: {}", e.getMessage());
            return null;
        }
    }
}
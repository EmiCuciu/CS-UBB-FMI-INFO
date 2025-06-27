package com.client;

import com.server.ClientHandler;
import com.server.protocol.Request;
import com.server.protocol.Response;
import com.services.GameObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClient {
    private static final Logger logger = LogManager.getLogger(GameClient.class);

    private String serverHost;
    private int serverPort;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean running;

    // For handling async responses from server
    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
    private final List<ClientObserver> observers = new ArrayList<>();

    public GameClient(Properties clientProps) {
        this.serverHost = clientProps.getProperty("server.host", "localhost");
        this.serverPort = Integer.parseInt(clientProps.getProperty("server.port", "55556"));
    }

    public void start() throws IOException {
        logger.info("Connecting to server {}:{}", serverHost, serverPort);
        connection = new Socket(serverHost, serverPort);

        // Important: Create output stream first to avoid deadlock
        output = new ObjectOutputStream(connection.getOutputStream());
        input = new ObjectInputStream(connection.getInputStream());

        running = true;

        // Start response reader thread
        new Thread(this::readResponses).start();

        logger.info("Connected to server");
    }

    public void stop() {
        running = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            logger.info("Disconnected from server");
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        }
    }

    public void addObserver(ClientObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ClientObserver observer) {
        observers.remove(observer);
    }

    public void login(String playerAlias) throws IOException {
        sendRequest(new Request(Request.RequestType.LOGIN, playerAlias));
    }

    public void startGame() throws IOException {
        sendRequest(new Request(Request.RequestType.START_GAME, null));
    }

    public void submitWord(Integer gameId, String word) throws IOException {
        sendRequest(new Request(Request.RequestType.SUBMIT_WORD,
                new ClientHandler.SubmitWordData(gameId, word)));
    }

    public void finishGame(Integer gameId) throws IOException {
        sendRequest(new Request(Request.RequestType.FINISH_GAME, gameId));
    }

    public void logout() throws IOException {
        sendRequest(new Request(Request.RequestType.LOGOUT, null));
    }

    private void sendRequest(Request request) throws IOException {
        synchronized (output) {
            output.writeObject(request);
            output.flush();
        }
    }

    private void readResponses() {
        while (running) {
            try {
                Response response = (Response) input.readObject();
                processResponse(response);
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    logger.error("Error reading from server", e);
                    stop();
                    break;
                }
            }
        }
    }

    private void processResponse(Response response) {
        // Notify observers about the response
        for (ClientObserver observer : observers) {
            observer.responseReceived(response);
        }

        // Add to queue for synchronous operations
        responseQueue.add(response);
    }

    public Response waitForResponse() throws InterruptedException {
        return responseQueue.take();
    }
}
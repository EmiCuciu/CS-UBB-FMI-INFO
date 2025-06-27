package com.server;

import com.model.Game;
import com.server.protocol.Request;
import com.server.protocol.Response;
import com.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable, GameObserver {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;
    private final GameService gameService;
    private final GameServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean running = true;
    private String currentPlayer;

    public ClientHandler(Socket socket, GameService gameService, GameServer server) {
        this.socket = socket;
        this.gameService = gameService;
        this.server = server;

        // Register as observer for ranking updates
        gameService.addObserver(this);

        try {
            // Important: Create output stream first to avoid deadlock
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.error("Error initializing streams for client", e);
            close();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Request request = (Request) input.readObject();
                handleRequest(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error handling client connection", e);
        } finally {
            close();
        }
    }

    private void handleRequest(Request request) {
        logger.info("Received request: {}", request.getType());

        try {
            switch (request.getType()) {
                case LOGIN:
                    handleLogin((String) request.getData());
                    break;
                case START_GAME:
                    handleStartGame();
                    break;
                case SUBMIT_WORD:
                    handleWordSubmission((SubmitWordData) request.getData());
                    break;
                case FINISH_GAME:
                    handleFinishGame((Integer) request.getData());
                    break;
                case LOGOUT:
                    handleLogout();
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing request", e);
            sendResponse(new Response(Response.ResponseType.ERROR, e.getMessage()));
        }
    }

    private void handleLogin(String playerAlias) {
        logger.info("Login request from player: {}", playerAlias);
        currentPlayer = playerAlias;
        sendResponse(new Response(Response.ResponseType.SUCCESS, "Logged in successfully"));
    }

    private void handleStartGame() throws ServiceException {
        logger.info("Start game request from player: {}", currentPlayer);
        Game game = gameService.startGame(currentPlayer);
        sendResponse(new Response(Response.ResponseType.GAME_STARTED, game));
    }

    private void handleWordSubmission(SubmitWordData data) throws ServiceException {
        logger.info("Word submission for game {}: {}", data.getGameId(), data.getWord());
        GuessResult result = gameService.processWordGuess(data.getGameId(), data.getWord());
        sendResponse(new Response(Response.ResponseType.WORD_RESULT, result));
    }

    private void handleFinishGame(Integer gameId) throws ServiceException {
        logger.info("Finish game request for game: {}", gameId);
        GameResult result = gameService.finishGame(gameId);
        sendResponse(new Response(Response.ResponseType.GAME_OVER, result));
    }

    private void handleLogout() {
        logger.info("Logout request from player: {}", currentPlayer);
        running = false;
        sendResponse(new Response(Response.ResponseType.SUCCESS, "Logged out successfully"));
    }

    public void sendRankingUpdate(List<Game> ranking) {
        if (running) {
            sendResponse(new Response(Response.ResponseType.RANKING_UPDATE, ranking));
        }
    }

    private void sendResponse(Response response) {
        try {
            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            logger.error("Error sending response to client", e);
            close();
        }
    }

    @Override
    public void rankingUpdated(List<Game> newRanking) {
        sendRankingUpdate(newRanking);
    }

    public void close() {
        running = false;
        gameService.removeObserver(this);
        server.removeClient(this);

        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException e) {
            logger.error("Error closing client handler resources", e);
        }
    }

    // Helper class for word submission data
    public static class SubmitWordData implements java.io.Serializable {
        private final Integer gameId;
        private final String word;

        public SubmitWordData(Integer gameId, String word) {
            this.gameId = gameId;
            this.word = word;
        }

        public Integer getGameId() {
            return gameId;
        }

        public String getWord() {
            return word;
        }
    }
}
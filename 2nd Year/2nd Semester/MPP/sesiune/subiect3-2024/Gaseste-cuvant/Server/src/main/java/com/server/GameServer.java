package com.server;

import com.model.Configuration;
import com.model.Game;
import com.persistence.*;
import com.server.protocol.Request;
import com.server.protocol.Response;
import com.server.rest.RestServer;
import com.services.GameObserver;
import com.services.GameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private static final Logger logger = LogManager.getLogger(GameServer.class);
    private final int port;
    private ServerSocket serverSocket;
    private final GameService gameService;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private boolean running;
    private final ExecutorService executor;
    private final RestServer restServer;

    public GameServer(Properties properties) {
        logger.info("Starting server...");
        port = Integer.parseInt(properties.getProperty("server.port"));

        // Initialize repositories
        IPlayerRepository playerRepository = new PlayerRepository();
        IConfigurationRepository configRepository = new ConfigurationRepository();
        IGameRepository gameRepository = new GameRepository();

        // Initialize service
        gameService = new GameService(gameRepository, playerRepository, configRepository);

        // Initialize REST server
        restServer = new RestServer(gameService);

        executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2
        );
    }

    public void start() {
        logger.info("Starting server on port {}", port);
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            // Start REST Server
            restServer.start();
            logger.info("Server started. Waiting for clients...");
            while (running) {
                Socket socket = serverSocket.accept();
                logger.info("Client connected from {}", socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket, gameService, this);
                clients.add(handler);
                executor.execute(handler);
            }
        } catch (IOException e) {
            logger.error("Error starting server", e);
        } finally {
            stop();
        }
    }

    public void stop() {
        logger.info("Stopping server...");
        running = false;

        // Close client connections
        for (ClientHandler client : clients) {
            client.close();
        }

        // Shutdown thread pool
        executor.shutdown();

        // Stop REST server
        restServer.stop();

        // Close server socket
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error("Error closing server socket", e);
            }
        }

        // Close Hibernate session factory
        HibernateUtils.closeSessionFactory();

        logger.info("Server stopped");
    }

    public void broadcastRankingUpdate(List<Game> ranking) {
        logger.info("Broadcasting ranking update to {} clients", clients.size());
        for (ClientHandler client : clients) {
            client.sendRankingUpdate(ranking);
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        logger.info("Client disconnected. Remaining clients: {}", clients.size());
    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(GameServer.class.getResourceAsStream("/server.properties"));
            GameServer server = new GameServer(serverProps);
            server.start();
        } catch (IOException e) {
            logger.error("Error loading server properties", e);
        }
    }
}
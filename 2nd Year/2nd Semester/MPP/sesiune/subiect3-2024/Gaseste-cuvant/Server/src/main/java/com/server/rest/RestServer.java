package com.server.rest;

import com.model.Configuration;
import com.model.Game;
import com.services.GameService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

public class RestServer {
    private static final Logger logger = LogManager.getLogger(RestServer.class);
    private final GameService gameService;
    private HttpServer server;
    private final int port = 8080;

    public RestServer(GameService gameService) {
        this.gameService = gameService;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/api/games", new GamesHandler());
            server.createContext("/api/configurations", new ConfigurationsHandler());
            server.setExecutor(Executors.newFixedThreadPool(4));
            server.start();
            logger.info("REST Server started on port {}", port);
        } catch (IOException e) {
            logger.error("Failed to start REST server", e);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("REST Server stopped");
        }
    }

    private class GamesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if ("GET".equals(exchange.getRequestMethod())) {
                    logger.info("GET request to /api/games");

                    // Extract query parameters
                    String query = exchange.getRequestURI().getQuery();
                    String playerAlias = null;
                    int minWords = 2; // Default value

                    if (query != null) {
                        String[] params = query.split("&");
                        for (String param : params) {
                            String[] keyValue = param.split("=");
                            if (keyValue.length == 2) {
                                if ("player".equals(keyValue[0])) {
                                    playerAlias = keyValue[1];
                                } else if ("minWords".equals(keyValue[0])) {
                                    minWords = Integer.parseInt(keyValue[1]);
                                }
                            }
                        }
                    }

                    if (playerAlias != null) {
                        List<Game> games = gameService.getGamesWithMinCorrectWords(playerAlias, minWords);
                        sendJsonResponse(exchange, 200, convertGamesToJson(games));
                    } else {
                        sendJsonResponse(exchange, 400, "{\"error\": \"Missing player parameter\"}");
                    }
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
            } catch (Exception e) {
                logger.error("Error handling games request", e);
                sendJsonResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
            } finally {
                exchange.close();
            }
        }

        private String convertGamesToJson(List<Game> games) {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                json.append("{")
                        .append("\"id\":").append(game.getId()).append(",")
                        .append("\"player\":\"").append(game.getPlayer().getAlias()).append("\",")
                        .append("\"score\":").append(game.getScore()).append(",")
                        .append("\"correctWords\":").append(game.getNrOfCorrectWords()).append(",")
                        .append("\"startTime\":\"").append(game.getStartingTime()).append("\"")
                        .append("}");

                if (i < games.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            return json.toString();
        }
    }

    private class ConfigurationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if ("POST".equals(exchange.getRequestMethod())) {
                    logger.info("POST request to /api/configurations");

                    // Parse request body
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                    StringBuilder requestBody = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        requestBody.append(line);
                    }

                    // Simple JSON parsing without external libraries
                    String json = requestBody.toString();
                    Configuration config = parseConfigurationJson(json);

                    gameService.addConfiguration(config);
                    sendJsonResponse(exchange, 201, "{\"status\": \"created\"}");
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
            } catch (Exception e) {
                logger.error("Error handling configuration request", e);
                sendJsonResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
            } finally {
                exchange.close();
            }
        }

        private Configuration parseConfigurationJson(String json) {
            // Very basic JSON parsing without external libraries
            // In production, use a proper JSON library
            String letters = extractJsonValue(json, "letters");
            String word1 = extractJsonValue(json, "word1");
            String word2 = extractJsonValue(json, "word2");
            String word3 = extractJsonValue(json, "word3");
            String word4 = extractJsonValue(json, "word4");

            return new Configuration(0, letters, word1, word2, word3, word4);
        }

        private String extractJsonValue(String json, String key) {
            int keyIndex = json.indexOf("\"" + key + "\"");
            if (keyIndex == -1) {
                throw new IllegalArgumentException("Missing required field: " + key);
            }

            int colonIndex = json.indexOf(":", keyIndex);
            int valueStartIndex = json.indexOf("\"", colonIndex) + 1;
            int valueEndIndex = json.indexOf("\"", valueStartIndex);

            return json.substring(valueStartIndex, valueEndIndex);
        }
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}
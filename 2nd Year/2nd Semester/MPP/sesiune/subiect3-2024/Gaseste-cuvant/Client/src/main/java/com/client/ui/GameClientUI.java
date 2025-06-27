package com.client.ui;

import com.client.ClientObserver;
import com.client.GameClient;
import com.model.Game;
import com.server.protocol.Response;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class GameClientUI extends Application implements ClientObserver {
    private static final Logger logger = LogManager.getLogger(GameClientUI.class);

    private GameClient client;
    private String currentPlayer;
    private Game currentGame;

    // UI components
    private Stage primaryStage;

    // Login scene components
    private TextField tfPlayerAlias;

    // Game scene components
    private Label lblGameInfo;
    private Label lblLetters;
    private TextField tfWordInput;
    private TextArea taGameLog;
    private Button btnStartGame;
    private Button btnSubmitWord;
    private Button btnFinishGame;

    // Ranking components
    private ListView<String> lvRanking;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize client
        try {
            Properties props = new Properties();
            InputStream is = getClass().getResourceAsStream("/client.properties");
            if (is != null) {
                props.load(is);
            }
            client = new GameClient(props);
            client.addObserver(this);
            client.start();
        } catch (IOException e) {
            logger.error("Error starting client", e);
            showErrorAndExit("Could not connect to server: " + e.getMessage());
            return;
        }

        showLoginScene();

        primaryStage.setTitle("Word Finding Game");
        primaryStage.setOnCloseRequest(e -> {
            try {
                if (currentPlayer != null) {
                    client.logout();
                }
                client.stop();
            } catch (IOException ex) {
                logger.error("Error during shutdown", ex);
            }
        });

        primaryStage.show();
    }

    private void showLoginScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label lblTitle = new Label("Word Finding Game");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblPrompt = new Label("Enter your player alias:");

        tfPlayerAlias = new TextField();
        tfPlayerAlias.setPromptText("Player Alias");
        tfPlayerAlias.setPrefWidth(200);

        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(e -> handleLogin());

        root.getChildren().addAll(lblTitle, lblPrompt, tfPlayerAlias, btnLogin);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showGameScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top section - Game info
        VBox topBox = new VBox(5);
        lblGameInfo = new Label("Welcome, " + currentPlayer);
        lblLetters = new Label("Available letters: ");
        topBox.getChildren().addAll(lblGameInfo, lblLetters);
        root.setTop(topBox);

        // Center section - Game log
        taGameLog = new TextArea();
        taGameLog.setEditable(false);
        taGameLog.setWrapText(true);
        taGameLog.setPrefHeight(200);
        root.setCenter(taGameLog);

        // Bottom section - Game controls
        HBox inputBox = new HBox(5);
        tfWordInput = new TextField();
        tfWordInput.setPromptText("Enter word");
        tfWordInput.setPrefWidth(200);
        btnSubmitWord = new Button("Submit Word");
        btnSubmitWord.setDisable(true);
        btnSubmitWord.setOnAction(e -> handleSubmitWord());
        inputBox.getChildren().addAll(tfWordInput, btnSubmitWord);

        HBox controlBox = new HBox(10);
        btnStartGame = new Button("Start Game");
        btnStartGame.setOnAction(e -> handleStartGame());
        btnFinishGame = new Button("Finish Game");
        btnFinishGame.setDisable(true);
        btnFinishGame.setOnAction(e -> handleFinishGame());
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> handleLogout());
        controlBox.getChildren().addAll(btnStartGame, btnFinishGame, btnLogout);

        VBox bottomBox = new VBox(10);
        bottomBox.getChildren().addAll(inputBox, controlBox);
        root.setBottom(bottomBox);

        // Right section - Rankings
        VBox rightBox = new VBox(5);
        Label lblRanking = new Label("Top Players");
        lvRanking = new ListView<>();
        lvRanking.setPrefWidth(150);
        rightBox.getChildren().addAll(lblRanking, lvRanking);
        root.setRight(rightBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
    }

    private void handleLogin() {
        String alias = tfPlayerAlias.getText().trim();
        if (alias.isEmpty()) {
            showError("Please enter a player alias");
            return;
        }

        try {
            client.login(alias);
            Response response = client.waitForResponse();

            if (response.getType() == Response.ResponseType.SUCCESS) {
                currentPlayer = alias;
                showGameScene();
            } else {
                showError("Login failed: " + response.getMessage());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error during login", e);
            showError("Error during login: " + e.getMessage());
        }
    }

    private void handleStartGame() {
        try {
            client.startGame();
            btnStartGame.setDisable(true);
            appendToLog("Starting a new game...");
        } catch (IOException e) {
            logger.error("Error starting game", e);
            showError("Error starting game: " + e.getMessage());
        }
    }

    private void handleSubmitWord() {
        if (currentGame == null) {
            showError("No active game");
            return;
        }

        String word = tfWordInput.getText().trim();
        if (word.isEmpty()) {
            showError("Please enter a word");
            return;
        }

        try {
            client.submitWord(currentGame.getId(), word);
            tfWordInput.clear();
        } catch (IOException e) {
            logger.error("Error submitting word", e);
            showError("Error submitting word: " + e.getMessage());
        }
    }

    private void handleFinishGame() {
        if (currentGame == null) {
            showError("No active game");
            return;
        }

        try {
            client.finishGame(currentGame.getId());
            btnSubmitWord.setDisable(true);
            btnFinishGame.setDisable(true);
            appendToLog("Finishing game...");
        } catch (IOException e) {
            logger.error("Error finishing game", e);
            showError("Error finishing game: " + e.getMessage());
        }
    }

    private void handleLogout() {
        try {
            client.logout();
            client.stop();
            showLoginScene();
            currentPlayer = null;
            currentGame = null;
        } catch (IOException e) {
            logger.error("Error during logout", e);
            showError("Error during logout: " + e.getMessage());
        }
    }

    @Override
    public void responseReceived(Response response) {
        Platform.runLater(() -> {
            switch (response.getType()) {
                case GAME_STARTED:
                    handleGameStarted(response);
                    break;
                case WORD_RESULT:
                    handleWordResult(response);
                    break;
                case GAME_OVER:
                    handleGameOver(response);
                    break;
                case RANKING_UPDATE:
                    handleRankingUpdate(response);
                    break;
                case ERROR:
                    appendToLog("Error: " + response.getMessage());
                    break;
            }
        });
    }

    private void handleGameStarted(Response response) {
        currentGame = (Game) response.getData();
        btnStartGame.setDisable(true);
        btnSubmitWord.setDisable(false);
        btnFinishGame.setDisable(false);

        lblLetters.setText("Available letters: " + currentGame.getConfiguration().getLetters());

        appendToLog("Game started! Available letters: " +
                currentGame.getConfiguration().getLetters());
        appendToLog("Find words using these letters. You have 4 attempts.");
    }

    private void handleWordResult(Response response) {
        com.services.GuessResult result = (com.services.GuessResult) response.getData();
        String message = result.getMessage();

        if (result.isCorrect()) {
            appendToLog("Correct! " + message + " (+" + result.getPoints() + " points)");
        } else {
            appendToLog("Incorrect. " + message);
        }

        if (result.isGameOver()) {
            btnSubmitWord.setDisable(true);
            btnFinishGame.setDisable(false);
            appendToLog("No more attempts left. Please finish the game.");
        }
    }

    private void handleGameOver(Response response) {
        com.services.GameResult result = (com.services.GameResult) response.getData();

        appendToLog("Game finished!");
        appendToLog("Final score: " + result.getGame().getScore());
        appendToLog("Words found: " + result.getGame().getNrOfCorrectWords() + "/4");
        appendToLog("Your ranking position: " + result.getRankingPosition());

        StringBuilder possibleWords = new StringBuilder("All possible words: ");
        for (String word : result.getAllPossibleWords()) {
            possibleWords.append(word).append(", ");
        }
        appendToLog(possibleWords.toString());

        currentGame = null;
        btnStartGame.setDisable(false);
        btnSubmitWord.setDisable(true);
        btnFinishGame.setDisable(true);
    }

    private void handleRankingUpdate(Response response) {
        @SuppressWarnings("unchecked")
        List<Game> ranking = (List<Game>) response.getData();

        lvRanking.getItems().clear();

        for (int i = 0; i < ranking.size(); i++) {
            Game game = ranking.get(i);
            lvRanking.getItems().add(
                    (i + 1) + ". " + game.getPlayer().getAlias() + ": " + game.getScore()
            );
        }
    }

    private void appendToLog(String message) {
        taGameLog.appendText(message + "\n");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAndExit(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fatal Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
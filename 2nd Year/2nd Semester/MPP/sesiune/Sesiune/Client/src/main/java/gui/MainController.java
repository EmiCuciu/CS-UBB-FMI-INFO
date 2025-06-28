package gui;

import domain.Joc;
import domain.Jucator;
import domain.RaspunsJucator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable, IObserver {
    private static Logger logger = LogManager.getLogger(MainController.class);

    private IServices server;
    private Jucator currentJucator;
    private ObservableList<Joc> gamesList = FXCollections.observableArrayList();
    private ObservableList<RaspunsJucator> answersList = FXCollections.observableArrayList();

    @FXML private Label lblWelcome;
    @FXML private Label lblPlayerInfo;
    @FXML private Label lblSelectedGame;
    @FXML private Label lblStatus;

    @FXML private TableView<Joc> tblGames;
    @FXML private TableColumn<Joc, Integer> colGameId;
    @FXML private TableColumn<Joc, LocalDateTime> colStartDate;
    @FXML private TableColumn<Joc, LocalDateTime> colEndDate;
    @FXML private TableColumn<Joc, Integer> colScore;
    @FXML private TableColumn<Joc, String> colStatus;
    @FXML private TableColumn<Joc, Integer> colLevel;

    @FXML private TableView<RaspunsJucator> tblAnswers;
    @FXML private TableColumn<RaspunsJucator, String> colQuestion;
    @FXML private TableColumn<RaspunsJucator, String> colYourAnswer;
    @FXML private TableColumn<RaspunsJucator, String> colCorrectAnswer;
    @FXML private TableColumn<RaspunsJucator, Integer> colPoints;

    @FXML private Button btnNewGame;
    @FXML private Button btnRefresh;
    @FXML private Button btnLogout;

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setJucator(Jucator jucator) {
        this.currentJucator = jucator;
        Platform.runLater(() -> {
            lblPlayerInfo.setText("Welcome, " + jucator.getnume() + "!");
            lblPlayerInfo.setText("Player ID: " + jucator.getId());
            loadGames();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colGameId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("dataInceput"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("dataSfarsit"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("punctajTotal"));
        colStatus.setCellValueFactory(cellData ->
                cellData.getValue().isFinalizatCuSucces() ?
                        javafx.beans.binding.Bindings.createStringBinding(() -> "Completed") :
                        javafx.beans.binding.Bindings.createStringBinding(() -> "In Progress"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("nivelCurent"));

        colQuestion.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getIntrebare().getText()));
        colYourAnswer.setCellValueFactory(new PropertyValueFactory<>("raspunsJucator"));
        colCorrectAnswer.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getIntrebare().getRaspunsCorect()));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("punctaj"));

        tblGames.setItems(gamesList);
        tblAnswers.setItems(answersList);

        tblGames.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblSelectedGame.setText("Game ID: " + newSelection.getId());
                loadAnswersForGame(newSelection);
            }
        });
    }

    private void loadGames() {
        try {
            List<Joc> games = server.getJocuriByJucator(currentJucator);
            gamesList.setAll(games);
            lblStatus.setText("Status: Games loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading games", e);
            showErrorMessage("Could not load games: " + e.getMessage());
        }
    }

    private void loadAnswersForGame(Joc joc) {
        try {
            List<RaspunsJucator> answers = server.getRaspunsuriByJoc(joc);
            answersList.setAll(answers);
            lblStatus.setText("Status: Answers loaded for game " + joc.getId());
        } catch (Exception e) {
            logger.error("Error loading answers", e);
            showErrorMessage("Could not load answers: " + e.getMessage());
        }
    }

    @FXML
    public void onNewGameClick(ActionEvent event) {
        showInfoMessage("Starting a new game...");
    }

    @FXML
    public void onRefreshClick(ActionEvent event) {
        loadGames();
    }

    @FXML
    public void onLogoutClick(ActionEvent event) {
        try {
            server.logOut(currentJucator, this);
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();
        } catch (ServiceException e) {
            logger.error("Error during logout", e);
            showErrorMessage("Error during logout: " + e.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfoMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
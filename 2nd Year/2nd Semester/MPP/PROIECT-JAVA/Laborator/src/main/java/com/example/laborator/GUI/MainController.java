package com.example.laborator.GUI;

import com.example.laborator.Domain.Arbitru;
import com.example.laborator.Domain.Participant;
import com.example.laborator.Domain.Rezultat;
import com.example.laborator.Domain.TipProba;
import com.example.laborator.Service.AuthenticationService;
import com.example.laborator.Service.ParticipantService;
import com.example.laborator.Service.ProbaService;
import com.example.laborator.Service.RezultatService;
import com.example.laborator.Utils.Observer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class MainController implements Observer {
    @FXML
    private Label arbitruNameLabel;

    @FXML
    private TableView<Participant> participantsTable;

    @FXML
    private TableColumn<Participant, String> nameColumn;

    @FXML
    private TableColumn<Participant, Integer> totalPointsColumn;

    @FXML
    private TableView<Rezultat> resultsTable;

    @FXML
    private TableColumn<Rezultat, String> resultNameColumn;

    @FXML
    private TableColumn<Rezultat, Integer> pointsColumn;

    @FXML
    private ComboBox<Participant> participantComboBox;

    @FXML
    private TextField pointsField;

    @FXML
    private Button addResultButton;

    @FXML
    private Button logoutButton;

    private AuthenticationService authService;
    private ParticipantService participantService;
    private RezultatService rezultatService;
    private ProbaService probaService;
    private Arbitru currentArbitru;
    private TipProba currentProba;

    private final ObservableList<Participant> participantsModel = FXCollections.observableArrayList();
    private final ObservableList<Rezultat> resultsModel = FXCollections.observableArrayList();

    public void setServices(AuthenticationService authService) {
        this.authService = authService;
    }

    public void setParticipantService(ParticipantService participantService) {
        this.participantService = participantService;
        this.participantService.addObserver(this);
    }

    public void setRezultatService(RezultatService rezultatService) {
        this.rezultatService = rezultatService;
        this.rezultatService.addObserver(this);
    }

    public void setProbaService(ProbaService probaService) {
        this.probaService = probaService;
    }

    public void setArbitru(Arbitru arbitru) {
        this.currentArbitru = arbitru;
    }

    @FXML
    public void initialize() {
        if (currentArbitru != null) {
            currentProba = probaService.getProbaForArbitru(currentArbitru);
            arbitruNameLabel.setText("Arbitru: " + currentArbitru.getFirst_name() + " " + currentArbitru.getLast_name() + "          Proba: " + currentProba);

            // Configure table columns
            nameColumn.setCellValueFactory(param ->
                    new SimpleStringProperty(param.getValue().getLast_name() + " " + param.getValue().getFirst_name()));

            totalPointsColumn.setCellValueFactory(param ->
                    new SimpleIntegerProperty(participantService.calculateTotalScore(param.getValue())).asObject());

            resultNameColumn.setCellValueFactory(param ->
                    new SimpleStringProperty(param.getValue().getParticipant().getLast_name() + " " +
                            param.getValue().getParticipant().getFirst_name()));

            pointsColumn.setCellValueFactory(param ->
                    new SimpleIntegerProperty(param.getValue().getPunctaj()).asObject());

            // Set models
            participantsTable.setItems(participantsModel);
            resultsTable.setItems(resultsModel);

            // Load data
            loadParticipants();
            loadResultsForCurrentProba();

            // Configure ComboBox
            participantComboBox.setItems(participantsModel);
            participantComboBox.setPromptText("Select participant");
            participantComboBox.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if (participant == null) {
                        return "";
                    }
                    return participant.getFirst_name() + " " + participant.getLast_name();
                }

                @Override
                public Participant fromString(String string) {
                    // Not needed for display purposes
                    return null;
                }
            });
        }
    }

    private void loadParticipants() {
        List<Participant> participants = participantService.getAllParticipants();
        participantsModel.clear();
        participantsModel.addAll(participants);
    }

    private void loadResultsForCurrentProba() {
        if (currentProba != null) {
            List<Rezultat> results = rezultatService.getRezultateForProba(currentProba);
            resultsModel.clear();
            resultsModel.addAll(results);
        }
    }

    @FXML
    private void handleAddResult(ActionEvent event) {
        Participant selectedParticipant = participantComboBox.getValue();
        String pointsText = pointsField.getText();

        if (selectedParticipant == null) {
            showError("Error", "Please select a participant");
            return;
        }

        try {
            int points = Integer.parseInt(pointsText);
            if (points < 0) {
                showError("Invalid points", "Points must be a positive number");
                return;
            }

            rezultatService.addRezultat(selectedParticipant, currentArbitru, currentProba, points);
            pointsField.clear();
            participantComboBox.setValue(null);

        } catch (NumberFormatException e) {
            showError("Invalid input", "Points must be a number");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        authService.logout();
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        loadParticipants();
        loadResultsForCurrentProba();
    }
}
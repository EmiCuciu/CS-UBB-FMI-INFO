package triatlon.arbitru.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.arbitru.gui.Util;
import triatlon.model.Arbitru;
import triatlon.model.Participant;
import triatlon.model.Rezultat;
import triatlon.model.TipProba;
import triatlon.services.ITriatlonObserver;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController implements ITriatlonObserver {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    @FXML
    private Label arbitruNameLabel;
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Participant> participantsTable;
    @FXML
    private TableColumn<Participant, String> nameColumn;
    @FXML
    private TableColumn<Participant, Integer> totalPointsColumn;

    @FXML
    private ComboBox<Participant> participantComboBox;
    @FXML
    private TextField pointsField;
    @FXML
    private Button addResultButton;

    @FXML
    private TableView<Rezultat> resultsTable;
    @FXML
    private TableColumn<Rezultat, String> resultNameColumn;
    @FXML
    private TableColumn<Rezultat, Integer> pointsColumn;

    private ITriatlonServices server;
    private Arbitru currentArbitru;
    private TipProba currentProba;

    private final ObservableList<Participant> participantsModel = FXCollections.observableArrayList();
    private final ObservableList<Rezultat> resultsModel = FXCollections.observableArrayList();
    private final ObservableList<Participant> participantsWithoutPointsForCurentProba = FXCollections.observableArrayList();

    public void setServer(ITriatlonServices server) {
        this.server = server;
    }

    public void setArbitru(Arbitru arbitru) {
        this.currentArbitru = arbitru;
    }

    public void setProba(TipProba proba) {
        this.currentProba = proba;
    }

    @FXML
    public void initialize() {
        // Configure table columns
        nameColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(
                        param.getValue().getLast_name() + " " + param.getValue().getFirst_name()
                )
        );

        totalPointsColumn.setCellValueFactory(param -> {
            try {
                return new javafx.beans.property.SimpleIntegerProperty(
                        server.calculateTotalScore(param.getValue())
                ).asObject();
            } catch (TriatlonException e) {
                logger.error("Error calculating score", e);
                return new javafx.beans.property.SimpleIntegerProperty(0).asObject();
            }
        });

        resultNameColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(
                        param.getValue().getParticipant().getLast_name() + " " +
                                param.getValue().getParticipant().getFirst_name()
                )
        );

        pointsColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleIntegerProperty(
                        param.getValue().getPunctaj()
                ).asObject()
        );

        // Set models
        participantsTable.setItems(participantsModel);
        resultsTable.setItems(resultsModel);

        // Configure participant combobox
        participantComboBox.setConverter(new javafx.util.StringConverter<Participant>() {
            @Override
            public String toString(Participant participant) {
                if (participant == null) return "";
                return participant.getFirst_name() + " " + participant.getLast_name();
            }

            @Override
            public Participant fromString(String string) {
                return null; // Not used for ComboBox
            }
        });
        participantComboBox.setItems(participantsModel);
    }

    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet() called, " +
                "currentArbitru: " + currentArbitru +
                ", currentProba: " + currentProba);

        participantComboBox.setItems(participantsWithoutPointsForCurentProba);
        participantComboBox.setCellFactory(lv -> new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLast_name() + " " + item.getFirst_name());
                }
            }
        });
        participantComboBox.setConverter(new javafx.util.StringConverter<Participant>() {
            @Override
            public String toString(Participant participant) {
                if (participant == null) return null;
                return participant.getLast_name() + " " + participant.getFirst_name();
            }

            @Override
            public Participant fromString(String string) {
                return null; // Not needed for this case
            }
        });

        updateParticipantsWithoutPoints();

        // Set the label text with arbitru and proba information
        if (currentArbitru != null) {
            String probaInfo = (currentProba != null) ?
                    "Proba: " + currentProba.getDenumire() :
                    "No proba assigned";

            arbitruNameLabel.setText("Arbitru: " + currentArbitru.getFirst_name() + " " +
                    currentArbitru.getLast_name() + " - " + probaInfo);

            // Load data after properties are set
            loadData();
        } else {
            logger.error("Unable to initialize main screen: arbitru is null");
        }
    }

    private void loadData() {
        try {
            // Load all participants
            List<Participant> participants = server.getAllParticipants();
            participantsModel.clear();
            participantsModel.addAll(participants);

            // Load results for current proba if it exists
            if (currentProba != null) {
                List<Rezultat> rezultate = server.getResultateForProba(currentProba);
                resultsModel.clear();
                resultsModel.addAll(rezultate);
            } else {
                resultsModel.clear(); // Just clear the model if there's no proba
            }
        } catch (TriatlonException e) {
            logger.error("Error loading data", e);
            showErrorMessage("Data Loading Error", "Failed to load data: " + e.getMessage());
        }
    }

    private void updateParticipantsWithoutPoints() {
        try {
            // Clear the current list
            participantsWithoutPointsForCurentProba.clear();

            // Get all participants
            List<Participant> allParticipants = server.getAllParticipants();

            // Get results for the current proba
            List<Rezultat> rezultateForProba = server.getResultateForProba(currentProba);

            // Create a set of participant IDs who already have results
            Set<Integer> participantsWithResults = rezultateForProba.stream()
                    .map(r -> r.getParticipant().getId())
                    .collect(Collectors.toSet());

            // Add only participants who don't have results for this proba
            for (Participant p : allParticipants) {
                if (!participantsWithResults.contains(p.getId())) {
                    participantsWithoutPointsForCurentProba.add(p);
                }
            }

            logger.info("Found {} participants without points for {}",
                    participantsWithoutPointsForCurentProba.size(), currentProba);
        } catch (TriatlonException e) {
            logger.error("Error updating participants without points: {}", e.getMessage());
            Util.showWarning("Error", "Could not update participants list: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddResult() {
        try {
            Participant selectedParticipant = participantComboBox.getValue();
            if (selectedParticipant == null) {
                showErrorMessage("Error", "Please select a participant");
                return;
            }

            String pointsText = pointsField.getText().trim();
            if (pointsText.isEmpty()) {
                showErrorMessage("Error", "Please enter points");
                return;
            }

            int points;
            try {
                points = Integer.parseInt(pointsText);
                if (points < 0) {
                    showErrorMessage("Error", "Points must be a positive number");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Error", "Points must be a valid number");
                return;
            }

            server.addRezultat(selectedParticipant, currentArbitru, currentProba, points);

            // Clear fields after successful addition
            pointsField.clear();
            participantComboBox.setValue(null);

        } catch (TriatlonException e) {
            logger.error("Error adding result", e);
            showErrorMessage("Error", "Could not add result: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            server.logout(currentArbitru, this);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();
        } catch (TriatlonException e) {
            logger.error("Error logging out", e);
            showErrorMessage("Error", "Could not log out: " + e.getMessage());
        }
    }

    private void showErrorMessage(String title, String message) {
        Util.showWarning(title, message);
    }

    // ITriatlonObserver implementation
    @Override
    public void arbitruLoggedIn(Arbitru arbitru) throws TriatlonException {
        // Not needed for UI updates
    }

    @Override
    public void arbitruLoggedOut(Arbitru arbitru) throws TriatlonException {
        // Not needed for UI updates
    }

    @Override
    public void rezultatAdded(Rezultat rezultat) throws TriatlonException {
        // Refresh data when a new result is added
        javafx.application.Platform.runLater(this::loadData);
        if (rezultat.getTipProba().equals(currentProba)) {
            Platform.runLater(this::updateParticipantsWithoutPoints);
        }
    }
}
package org.example.ati.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.ati.Domain.Pat;
import org.example.ati.Domain.TipPat;
import org.example.ati.Service.Service;
import org.example.ati.Utils.Events.Event;
import org.example.ati.Utils.Obs.Observer;

import java.util.Map;

public class PaturiController implements Observer {
    private Service service;

    @FXML
    private Label labelTICLibere;
    @FXML
    private Label labelTIMLibere;
    @FXML
    private Label labelTIIPLibere;
    @FXML
    private TextField txtCNPEliberare;
    @FXML
    private TableView<Pat> tablePaturi;
    @FXML
    private TableColumn<Pat, Integer> colId;
    @FXML
    private TableColumn<Pat, String> colTip;
    @FXML
    private TableColumn<Pat, Boolean> colVentilatie;
    @FXML
    private TableColumn<Pat, String> colPacient;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initializeTable();
        updateStatusPaturi();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTip.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTip().name()));
        colVentilatie.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().hasVentilatie()));
        colPacient.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPacientCnp()));
    }

    private void initializeTable() {
        tablePaturi.getItems().clear();
        tablePaturi.getItems().addAll(service.getAllPaturi());
    }

    private void updateStatusPaturi() {
        Map<TipPat, Integer> status = service.getStatusPaturiLibere();

        // Update TIC label
        int ticLibere = status.getOrDefault(TipPat.TIC, 0);
        labelTICLibere.setText("TIC: " + ticLibere + " paturi libere");
        labelTICLibere.setTextFill(ticLibere == 0 ? Color.RED : Color.GREEN);

        // Update TIM label
        int timLibere = status.getOrDefault(TipPat.TIM, 0);
        labelTIMLibere.setText("TIM: " + timLibere + " paturi libere");
        labelTIMLibere.setTextFill(timLibere == 0 ? Color.RED : Color.GREEN);

        // Update TIIP label
        int tiipLibere = status.getOrDefault(TipPat.TIIP, 0);
        labelTIIPLibere.setText("TIIP: " + tiipLibere + " paturi libere");
        labelTIIPLibere.setTextFill(tiipLibere == 0 ? Color.RED : Color.GREEN);
    }

    @FXML
    private void handleElibereazaPat() {
        String cnp = txtCNPEliberare.getText().trim();
        if (cnp.isEmpty()) {
            showAlert("Error", "Introduceți CNP-ul pacientului!");
            return;
        }

        try {
            service.elibereazaPat(cnp);
            txtCNPEliberare.clear();
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @Override
    public void update(Event event) {
        Platform.runLater(() -> {
            initializeTable();
            updateStatusPaturi();
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
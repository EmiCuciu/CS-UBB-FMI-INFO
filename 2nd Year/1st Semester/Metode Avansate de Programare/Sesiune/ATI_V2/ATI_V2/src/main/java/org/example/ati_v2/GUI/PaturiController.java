package org.example.ati_v2.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.ati_v2.Domain.Pat;
import org.example.ati_v2.Domain.TipPat;
import org.example.ati_v2.Service.Service;
import org.example.ati_v2.Utils.Events.Event;
import org.example.ati_v2.Utils.Obs.Observer;

import java.util.Map;

public class PaturiController implements Observer {
    private Service service;

    @FXML
    public Label label_LOCURI_Libere;
    @FXML
    public Label labelTICLibere;
    @FXML
    public Label labelTIMLibere;
    @FXML
    public Label labelTIIPLibere;
    @FXML
    public TableView<Pat> tablePaturi;
    @FXML
    public TableColumn<Pat, Integer> colID;
    @FXML
    public TableColumn<Pat, String> colTip;
    @FXML
    public TableColumn<Pat, Boolean> colVentilatie;
    @FXML
    public TableColumn<Pat, String> colPacient;
    @FXML
    public TextField txtCNPEliberare;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initializaTable();
        updateStatusPaturi();
    }

    @FXML
    public void initialize() {
        colID.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTip.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTip().name()));
        colVentilatie.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().hasVentilatie()));
        colPacient.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPacientCNP()));
    }

    private void initializaTable() {
        tablePaturi.getItems().clear();
        tablePaturi.getItems().addAll(service.getAllPaturi());
    }

    private void updateStatusPaturi() {
        Map<TipPat, Integer> status = service.getStatusPaturiLibere();
        int totalLibere = status.values().stream().mapToInt(Integer::intValue).sum();

        label_LOCURI_Libere.setText("Locuri Libere: " + totalLibere);
        label_LOCURI_Libere.setTextFill(Color.TURQUOISE);

        int ticLibere = status.getOrDefault(TipPat.TIC, 0);
        labelTICLibere.setText("TIC: " + ticLibere + " paturi libere");
        labelTICLibere.setTextFill(ticLibere == 0 ? Color.RED : Color.GREEN);

        int timLibere = status.getOrDefault(TipPat.TIM, 0);
        labelTIMLibere.setText("TIM: " + timLibere + " paturi libere");
        labelTIMLibere.setTextFill(timLibere == 0 ? Color.RED : Color.GREEN);

        int tiipLibere = status.getOrDefault(TipPat.TIIP, 0);
        labelTIIPLibere.setText("TIIP: " + tiipLibere + " paturi libere");
        labelTIIPLibere.setTextFill(tiipLibere == 0 ? Color.RED : Color.GREEN);
    }

    @FXML
    private void handleElibereazaPat() {
        String cnp = txtCNPEliberare.getText().trim();
        if (cnp.isEmpty()) {
            showAlert("Error", "Introduceti CNP-ul pacientului!");
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
            initializaTable();
            updateStatusPaturi();
        });
    }

    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package org.example.ati.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ati.Domain.Pacient;
import org.example.ati.Domain.Pat;
import org.example.ati.Domain.TipPat;
import org.example.ati.Service.Service;
import org.example.ati.Utils.Events.Event;
import org.example.ati.Utils.Obs.Observer;

import java.util.List;

public class PacientiController implements Observer {
    private Service service;

    @FXML
    private TableView<Pacient> tablePacienti;
    @FXML
    private TableColumn<Pacient, String> colCNP;
    @FXML
    private TableColumn<Pacient, Integer> colGravitate;
    @FXML
    private TableColumn<Pacient, String> colDiagnostic;
    @FXML
    private ComboBox<TipPat> comboTipPat;
    @FXML
    private CheckBox checkVentilatie;
    @FXML
    private ComboBox<Integer> comboIdPat;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initializeTable();
        initializeComboBoxes();
    }

    @FXML
    public void initialize() {
        colCNP.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colGravitate.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGravitate()).asObject());
        colDiagnostic.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDiagnosticPrincipal()));

        comboTipPat.getItems().addAll(TipPat.values());
        comboTipPat.setOnAction(e -> updatePaturiDisponibile());
    }

    private void initializeTable() {
        List<Pacient> pacientiInAsteptare = service.getPacientiInAsteptare();
        tablePacienti.getItems().clear();
        tablePacienti.getItems().addAll(pacientiInAsteptare);
    }

    private void initializeComboBoxes() {
        comboTipPat.getItems().clear();
        comboTipPat.getItems().addAll(TipPat.values());
    }

    private void updatePaturiDisponibile() {
        TipPat tipSelectat = comboTipPat.getValue();
        if (tipSelectat != null) {
            List<Pat> paturiDisponibile = service.getPaturiByTip(tipSelectat);
            comboIdPat.getItems().clear();
            paturiDisponibile.stream()
                    .filter(Pat::isLiber)
                    .map(Pat::getId)
                    .forEach(comboIdPat.getItems()::add);
        }
    }

    @FXML
    private void handleAsigneazaPat() {
        Pacient pacientSelectat = tablePacienti.getSelectionModel().getSelectedItem();
        Integer idPatSelectat = comboIdPat.getValue();

        if (pacientSelectat == null || idPatSelectat == null) {
            showAlert("Error", "Selectați un pacient și un pat!");
            return;
        }

        try {
            service.asigneazaPat(pacientSelectat.getId(), idPatSelectat, checkVentilatie.isSelected());
        } catch (IllegalStateException | IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @Override
    public void update(Event event) {
        Platform.runLater(() -> {
            initializeTable();
            updatePaturiDisponibile();
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
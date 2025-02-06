package org.example.ati_v2.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ati_v2.Domain.Pacient;
import org.example.ati_v2.Domain.Pat;
import org.example.ati_v2.Domain.TipPat;
import org.example.ati_v2.Service.Service;
import org.example.ati_v2.Utils.Events.Event;
import org.example.ati_v2.Utils.Obs.Observer;

import java.util.List;

public class PacientiController implements Observer {
    private Service service;

    @FXML
    public TableView<Pacient> TabelPacienti;
    @FXML
    public TableColumn<Pacient, String> colCNP;
    @FXML
    public TableColumn<Pacient, Integer> colGravitate;
    @FXML
    public TableColumn<Pacient, String> colDiagnostic;
    @FXML
    public ComboBox<TipPat> comboTipPat;
    @FXML
    public CheckBox checkVentilatie;
    @FXML
    public ComboBox<Integer> comboIDPat;

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
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDiagnostic()));

        comboTipPat.getItems().addAll(TipPat.values());
        comboTipPat.setOnAction(e -> updatePaturiDisponibile());
    }

    private void initializeTable(){
        List<Pacient> pacientiInAsteptare = service.getPacientiInAsteptare();
        TabelPacienti.getItems().clear();
        TabelPacienti.getItems().addAll(pacientiInAsteptare);
    }

    private void initializeComboBoxes(){
        comboTipPat.getItems().clear();
        comboTipPat.getItems().addAll(TipPat.values());
    }

    private void updatePaturiDisponibile(){
        TipPat tipSelectat = comboTipPat.getValue();
        if(tipSelectat != null){
            List<Pat> paturiDisponibile = service.getPaturiByTip(tipSelectat);
            comboIDPat.getItems().clear();
            paturiDisponibile.stream()
                    .filter(Pat::isLiber)
                    .map(Pat::getId)
                    .forEach(comboIDPat.getItems()::add);
        }
    }

    @FXML
    private void handleAsigneazaPat(){
        Pacient pacientSelectat = TabelPacienti.getSelectionModel().getSelectedItem();
        Integer idPatSelectat = comboIDPat.getValue();

        if (pacientSelectat == null || idPatSelectat == null){
            showAlert("Error", "Selectati un pacient si un pat!");
            return;
        }

        try{
            service.asigneazaPat(pacientSelectat.getId(), idPatSelectat, checkVentilatie.isSelected());
        }
        catch (IllegalArgumentException | IllegalStateException e){
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

    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

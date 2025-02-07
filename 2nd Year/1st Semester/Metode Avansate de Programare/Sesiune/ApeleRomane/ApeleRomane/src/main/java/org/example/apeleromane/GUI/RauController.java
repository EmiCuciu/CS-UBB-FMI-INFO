package org.example.apeleromane.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.apeleromane.Domain.Rau;
import org.example.apeleromane.Service.Service;
import org.example.apeleromane.Utils.Events.Event;
import org.example.apeleromane.Utils.Obs.Observer;

public class RauController implements Observer {
    private Service service;

    @FXML
    public TableView<Rau> RauriTableView;
    @FXML
    public TableColumn<Rau, String> NumeRauTableColumn;
    @FXML
    public TableColumn<Rau, Double> CotaMedieTableColumn;
    @FXML
    public TextField NouaCotaTextField;
    @FXML
    public Button ApplyButton;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initializeTable();
    }

    @FXML
    public void initialize(){
        NumeRauTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNume()));
        CotaMedieTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getCotaMedie()).asObject());

    }

    private void initializeTable(){
        RauriTableView.getItems().clear();
        RauriTableView.getItems().addAll(service.getAllRauri());
    }

    private void updateCotaMedie(){
        Rau selectedRau = RauriTableView.getSelectionModel().getSelectedItem();
        if(selectedRau == null){
            return;
        }
        try{
            double newCota = Double.parseDouble(NouaCotaTextField.getText());
            service.updateRauCota(selectedRau.getId(), newCota);
        } catch (RuntimeException rex){
            System.out.println(rex.getMessage());
        }
    }

    @FXML
    public void handleApplyNouaCota() {
        updateCotaMedie();
    }

    @Override
    public void update(Event event) {
        initializeTable();
    }
}

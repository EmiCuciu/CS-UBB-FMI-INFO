package org.example.apeleromane.GUI;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.apeleromane.Domain.Localitate;
import org.example.apeleromane.Domain.Rau;
import org.example.apeleromane.Domain.TipCotaPericol;
import org.example.apeleromane.Service.Service;
import org.example.apeleromane.Utils.Events.Event;
import org.example.apeleromane.Utils.Obs.Observer;

public class AvertizariController implements Observer {

    private Service service;

    @FXML
    private ListView<Localitate> localitatiListView;

    @FXML
    private CheckBox riscRedusCheckBox;

    @FXML
    private CheckBox riscMediuCheckBox;

    @FXML
    private CheckBox riscMajorCheckBox;

    @FXML
    public TableView<Object[]> RaulTranzitTableView;
    @FXML
    public TableColumn<Object[], String> NumeRauTranzitTableColumn;
    @FXML
    public TableColumn<Object[], Double> CotaRauTranzitTableColumn;
    @FXML
    public TableColumn<Object[], Double> CMDRauTranzitTableColumn;
    @FXML
    public TableColumn<Object[], Double> CMARauTranzitTableColumn;

    @FXML
    public TextField NumeTextField;
    @FXML
    public TextField CMDRTextField;
    @FXML
    public TextField CMATextField;
    @FXML
    public TextField REZULTATTextField;
    @FXML
    public Button CalculateRiskButton;


    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initializeComponents();
    }

    @FXML
    public void initialize() {
        riscRedusCheckBox.setSelected(true);
        riscMediuCheckBox.setSelected(true);
        riscMajorCheckBox.setSelected(true);

        riscRedusCheckBox.setOnAction(event -> updateLocalitatiList());
        riscMediuCheckBox.setOnAction(event -> updateLocalitatiList());
        riscMajorCheckBox.setOnAction(event -> updateLocalitatiList());

        localitatiListView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Localitate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item.getNume());
                    switch (item.calculeazaRisc()) {
                        case REDUS -> setTextFill(Color.GREEN);
                        case MEDIU -> setTextFill(Color.ORANGE);
                        case MAJOR -> setTextFill(Color.RED);
                    }
                }
            }
        });

        localitatiListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateRaulTranzitTable(newValue);
            }
        });

        NumeRauTranzitTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[0]));
        CotaRauTranzitTableColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue()[1]).asObject());
        CMDRauTranzitTableColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue()[2]).asObject());
        CMARauTranzitTableColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue()[3]).asObject());
    }

    private void updateRaulTranzitTable(Localitate localitate) {
        RaulTranzitTableView.getItems().clear();
        if (localitate.getRau() != null) {
            Rau rau = localitate.getRau();
            RaulTranzitTableView.getItems().add(new Object[]{
                    rau.getNume(),
                    rau.getCotaMedie(),
                    localitate.getCotaMinimaDeRisc(),
                    localitate.getCotaMaximaAdmisa()
            });
        }
    }

    private void initializeComponents() {
        updateLocalitatiList();
    }

    private void updateLocalitatiList() {
        localitatiListView.getItems().clear();
        if (riscRedusCheckBox.isSelected()) {
            localitatiListView.getItems().addAll(service.getLocalitatiByTip(TipCotaPericol.REDUS));
        }
        if (riscMediuCheckBox.isSelected()) {
            localitatiListView.getItems().addAll(service.getLocalitatiByTip(TipCotaPericol.MEDIU));
        }
        if (riscMajorCheckBox.isSelected()) {
            localitatiListView.getItems().addAll(service.getLocalitatiByTip(TipCotaPericol.MAJOR));
        }
    }

    @Override
    public void update(Event event) {
        updateLocalitatiList();
    }

    @FXML
    public void handleCalculateRisk() {
        String numeRau = NumeTextField.getText();
        double cmdr = Double.parseDouble(CMDRTextField.getText());
        double cma = Double.parseDouble(CMATextField.getText());

        Rau rau = service.findRauByName(numeRau);
        if (rau == null) {
            REZULTATTextField.setText("Eroare: Râul nu este înregistrat.");
            return;
        }

        Localitate localitate = new Localitate(null, "Localitate Noua", rau, cmdr, cma);
        TipCotaPericol risc = localitate.calculeazaRisc();
        REZULTATTextField.setText("Grupa de risc: " + risc);
    }
}
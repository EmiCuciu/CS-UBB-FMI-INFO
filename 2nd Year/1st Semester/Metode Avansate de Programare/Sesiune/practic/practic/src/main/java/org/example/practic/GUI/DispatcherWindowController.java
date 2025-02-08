package org.example.practic.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.example.practic.Service.TaxiService;

public class DispatcherWindowController {
    @FXML private TextField pickupAddressField;
    @FXML private TextField destinationField;
    @FXML private TextField clientNameField;

    private TaxiService service;

    public void initialize(TaxiService service) {
        this.service = service;
    }

    @FXML
    private void handleAddOrder() {
        String pickup = pickupAddressField.getText().trim();
        String destination = destinationField.getText().trim();
        String clientName = clientNameField.getText().trim();

        if (pickup.isEmpty() || destination.isEmpty() || clientName.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required!").show();
            return;
        }

        service.addNewOrder(pickup, destination, clientName);

        pickupAddressField.clear();
        destinationField.clear();
        clientNameField.clear();
    }
}
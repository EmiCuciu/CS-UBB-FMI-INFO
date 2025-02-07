package org.example.zboruri.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.zboruri.Domain.Client;
import org.example.zboruri.Repository.ClientRepository;
import org.example.zboruri.Service.ClientService;
import org.example.zboruri.ZboruriApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private Label errorLabel;

    private final ClientService clientService;

    public LoginController() {
        this.clientService = new ClientService(new ClientRepository());
    }

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            errorLabel.setText("Username cannot be empty");
            return;
        }

        try {
            Client client = clientService.login(username);
            ZboruriApplication app = new ZboruriApplication();
            app.openMainInterface(client);
        } catch (RuntimeException | IOException e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
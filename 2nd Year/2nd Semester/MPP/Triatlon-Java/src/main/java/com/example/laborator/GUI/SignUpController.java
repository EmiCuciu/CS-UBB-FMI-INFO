package com.example.laborator.GUI;

import com.example.laborator.Service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    private AuthenticationService authService;

    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showErrorMessage("Registration error", "All fields are required");
            return;
        }

        // Register using the authentication service
        boolean registered = authService.register(username, password, firstName, lastName);

        if (registered) {
            showInfoMessage("Registration successful", "Your account has been created. You can now log in.");
            openLoginWindow();
        } else {
            showErrorMessage("Registration failed", "Username may already be taken");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        openLoginWindow();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator/login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setAuthService(authService);

            Stage stage = new Stage();
            stage.setTitle("Triatlon Competition - Login");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

            // Close signup window
            ((Stage) registerButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error", "Could not open login window");
        }
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
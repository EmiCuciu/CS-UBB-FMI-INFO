package com.example.laborator.GUI;

import com.example.laborator.Domain.Arbitru;
import com.example.laborator.Service.AuthenticationService;
import com.example.laborator.Service.ParticipantService;
import com.example.laborator.Service.ProbaService;
import com.example.laborator.Service.RezultatService;
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
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    private AuthenticationService authService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private RezultatService rezultatService;

    // Track logged-in arbitrators
    private final List<Integer> loggedInArbitratorIds = new ArrayList<>();
    private int successfulLogins = 0;
    private static final int MAX_LOGINS = 3;

    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    public void setServices(ParticipantService participantService, ProbaService probaService, RezultatService rezultatService) {
        this.participantService = participantService;
        this.probaService = probaService;
        this.rezultatService = rezultatService;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Login Error", "Username and password cannot be empty");
            return;
        }

        if (successfulLogins >= MAX_LOGINS) {
            showError("Login Limit", "Maximum number of arbitrators already logged in");
            return;
        }

        Arbitru loggedInArbitru = authService.login(username, password);
        if (loggedInArbitru != null) {
            // Open the main window for this arbitrator
            boolean success = openMainWindow(loggedInArbitru);
            if (success) {
                // Track this login
                loggedInArbitratorIds.add(loggedInArbitru.getId());
                successfulLogins++;

                // Clear the form for next login
                usernameField.clear();
                passwordField.clear();

                // If we've reached the limit, disable login and close window
                if (successfulLogins >= MAX_LOGINS) {
                    loginButton.setDisable(true);
                    showInfo("Login Limit", "Maximum number of arbitrators logged in. No more logins allowed.");
                    // Get stage from any control and close it
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();
                }
            }
        } else {
            showError("Login Error", "Invalid username or password");
        }
    }

    private boolean openMainWindow(Arbitru arbitru) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setServices(authService);
            mainController.setParticipantService(participantService);
            mainController.setRezultatService(rezultatService);
            mainController.setProbaService(probaService);
            mainController.setArbitru(arbitru);
            mainController.initialize();

            Stage stage = new Stage();
            stage.setTitle("Triatlon Competition - " + probaService.getProbaForArbitru(arbitru).getTipProba().getDenumire());
            stage.setScene(new Scene(root, 800, 600));
            stage.setOnCloseRequest(e -> {
                // Remove this arbitrator from logged-in list when window closes
                loggedInArbitratorIds.remove(Integer.valueOf(arbitru.getId()));
                successfulLogins--;
                loginButton.setDisable(false);
            });
            stage.show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open main application window");
            return false;
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator/signup.fxml"));
            Parent root = loader.load();

            SignUpController signupController = loader.getController();
            signupController.setAuthService(authService);

            Stage stage = new Stage();
            stage.setTitle("Triatlon Competition - Sign Up");
            stage.setScene(new Scene(root, 600, 450));
            stage.show();

            // Close login window
            ((Stage) signUpButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open signup window");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
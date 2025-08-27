package triatlon.arbitru.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.arbitru.gui.Util;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

public class SignUpController {
    private static final Logger logger = LogManager.getLogger(SignUpController.class);

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

    private ITriatlonServices server;

    public void setServer(ITriatlonServices server) {
        this.server = server;
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Util.showWarning("Registration Error", "All fields must be completed");
            return;
        }

        try {
            boolean result = server.register(username, password, firstName, lastName);
            if (result) {
                Util.showWarning("Success", "Registration successful. You can now log in.");
                handleBackToLogin(event);
            } else {
                Util.showWarning("Registration Error", "Username already exists");
            }
        } catch (TriatlonException e) {
            logger.error("Registration error", e);
            Util.showWarning("Registration Error", e.getMessage());
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setServer(server);

            Stage stage = new Stage();
            stage.setTitle("Triatlon Competition - Login");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) backToLoginButton.getScene().getWindow()).close();
        } catch (Exception e) {
            logger.error("Error returning to login", e);
            Util.showWarning("Error", "Could not return to login screen: " + e.getMessage());
        }
    }
}
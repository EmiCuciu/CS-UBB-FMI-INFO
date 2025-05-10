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
import triatlon.model.Arbitru;
import triatlon.model.TipProba;
import triatlon.services.ITriatlonServices;

import java.io.IOException;

public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    private ITriatlonServices server;

    public void setServer(ITriatlonServices server) {
        this.server = server;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Arbitru arbitru = server.findArbitruByUsernameAndPassword(username, password);
            System.out.println("Arbitru este: " + arbitru);
            if (arbitru != null) {
                TipProba proba = server.getProbaForArbitru(arbitru);
                System.out.println("PROBA ESTE: " + proba);


                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
                Parent root = loader.load();
                MainController mainController = loader.getController();

                // Set server, arbitru and proba first
                mainController.setServer(server);
                mainController.setArbitru(arbitru);
                mainController.setProba(proba);

                server.login(arbitru, mainController);

                mainController.afterPropertiesSet();

                Stage stage = new Stage();
                String probaTitle = (proba != null) ? proba.getDenumire() : "No proba assigned";
                stage.setTitle("Triatlon Competition - " + probaTitle);
                stage.setScene(new Scene(root));
                stage.show();

                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                Util.showWarning("Login Error", "Invalid username or password!");
            }
        } catch (Exception e) {
            logger.error("Login error", e);
            Util.showWarning("Error", e.getMessage());
        }
    }


    @FXML
    public void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("signup.fxml"));
            Parent root = loader.load();

            SignUpController signUpController = loader.getController();
            signUpController.setServer(server);

            Stage stage = new Stage();
            stage.setTitle("Triatlon Competition - Sign Up");
            stage.setScene(new Scene(root));
            stage.show();

            // Close login window
            ((Stage) signUpButton.getScene().getWindow()).close();
        } catch (IOException e) {
            logger.error("Could not open sign up window: {}", e.getMessage());
            Util.showWarning("Error", "Could not open sign up window: " + e.getMessage());
        }
    }
}
package org.example.lab8.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.example.lab8.domain.Utilizator;

public class LoginController {
    private Controller controller = ApplicationContext.getController();
    public static String logedInUsername;
    public static Utilizator logedInUser;

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        signUpButton.setOnAction(event -> SceneManager.switchScene("/org/example/lab8/signup.fxml"));
    }

    @FXML
    private void handleLogin() {
        String user = username.getText();
        String pass = password.getText();

        logedInUsername = user;
        logedInUser = controller.getService().findUtilizatorByUsername(logedInUsername);

        Utilizator utilizator = authenticate(user, pass);
        if (utilizator != null) {
            System.out.println("Username: " + user + ", Password: " + pass + " - Logged in!");
            SceneManager.switchScene("/org/example/lab8/main.fxml");
        } else {
            System.out.println("Invalid username or password.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    private Utilizator authenticate(String username, String password) {
        return controller.getService().findByUsernameAndPassword(username, password).orElse(null);
    }
}
package org.example.lab8.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.example.lab8.domain.Utilizator;

public class LoginController {
    private final Controller controller = ApplicationContext.getController();
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
        signUpButton.setOnAction(event -> handleSignUp());
    }

    @FXML
    private void handleSignUp() {
        SceneManager.switchScene("/org/example/lab8/signUp.fxml");
    }

    @FXML
    private void handleLogin() {
        String user = username.getText();
        String pass = password.getText();

        Utilizator utilizator = authenticate(user, pass);
        if (utilizator != null) {
            System.out.println("Username: " + user + ", Password: " + pass + " - Logged in!");

            logedInUsername = user;
            logedInUser = utilizator;

            SceneManager.openNewWindow("/org/example/lab8/main.fxml");
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
        return controller.getService().getUserService().findByUsernameAndPassword(username, password).orElse(null);
    }
}
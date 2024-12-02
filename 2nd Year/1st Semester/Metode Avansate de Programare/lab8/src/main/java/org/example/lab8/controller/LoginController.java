package org.example.lab8.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.lab8.domain.Utilizator;

import java.io.IOException;

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


        Utilizator utilizator = authenticate(user, pass);
        if (utilizator != null) {
            System.out.println("Username: " + user + ", Password: " + pass + " - Logged in!");

            logedInUsername = user;
            logedInUser = utilizator;

            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lab8/main.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Mafia Network");
                stage.setScene(new Scene(root));
                stage.getIcons().add(new javafx.scene.image.Image(String.valueOf(getClass().getResource("/org/example/lab8/images/money.png"))));
                stage.show();
            }
            catch (IOException e){
                e.printStackTrace();
            }

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
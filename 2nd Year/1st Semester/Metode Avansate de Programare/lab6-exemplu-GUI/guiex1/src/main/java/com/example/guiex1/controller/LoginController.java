package com.example.guiex1.controller;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;

    private Service service;
    private Stage primaryStage;


    public void setService(Service service) {
        System.out.println("Setting service in LoginController");
        this.service = service;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("Login");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Utilizator user = authenticate(username, password);
        if (user != null) {
            showMainApp(user);
            primaryStage.close();
        } else {
            System.out.println("Invalid username or password");
        }
    }

    @FXML
    private void handleSignUp() {
        showSignupDialog();
    }

    private Utilizator authenticate(String username, String password) {
        return service.findByUsernameAndPassword(username, password).orElse(null);
    }

    private void showMainApp(Utilizator user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guiex1/views/main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            MainController mainController = loader.getController();
            mainController.setService(service);
            mainController.setLoggedinUser(user);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSignupDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guiex1/views/signup.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setTitle("Sign Up");

            SignupController controller = loader.getController();
            controller.setService(service);
            controller.setPrimaryStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
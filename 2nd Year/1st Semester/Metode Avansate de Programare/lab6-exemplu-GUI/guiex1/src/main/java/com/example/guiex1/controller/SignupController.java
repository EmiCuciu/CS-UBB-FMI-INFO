package com.example.guiex1.controller;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.Service;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;

    private Service service;
    private Stage primaryStage;

    public void setService(Service service) {
        this.service = service;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void handleSignUpSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstnameField.getText();
        TextField lastnamefield = lastnameField;
        String lastName = lastnamefield.getText();
        Utilizator utilizator = new Utilizator(firstName, lastName);
        utilizator.setUsername(username);
        utilizator.setPassword(password);

        service.saveUser(utilizator);
        primaryStage.close();
    }
}
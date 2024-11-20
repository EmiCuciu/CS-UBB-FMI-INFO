package com.example.guiex1;

import com.example.guiex1.controller.Controller;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.dbrepo.DBRepository;
import com.example.guiex1.services.Service;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginApp extends Application {
    private Service service;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) {
        service = new Service(new DBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", new UtilizatorValidator()));
        controller = new Controller();
        controller.setService(service);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            Utilizator user = authenticate(username, password);
            if (user != null) {
                showMainApp(user);
                primaryStage.close();
            } else {
                // Show error message
                System.out.println("Invalid username or password");
            }
        });

        signupButton.setOnAction(e -> showSignupDialog());

        VBox vbox = new VBox(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signupButton);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Utilizator authenticate(String username, String password) {
        return service.findByUsernameAndPassword(username, password).orElse(null);
    }

    private void showMainApp(Utilizator user) {
        MainApp mainApp = new MainApp();
        mainApp.setUser(user);
        Stage stage = new Stage();
        try {
            mainApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSignupDialog() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        Button signupButton = new Button("Sign Up");

        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            Utilizator newUser = new Utilizator(firstName, lastName);
            newUser.setUsername(username);
            newUser.setPassword(password);
            service.saveUser(newUser);
            stage.close();
        });

        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, firstNameLabel, firstNameField, lastNameLabel, lastNameField, signupButton);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
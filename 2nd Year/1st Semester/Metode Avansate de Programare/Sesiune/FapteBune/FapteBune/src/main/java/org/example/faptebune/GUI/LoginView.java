package org.example.faptebune.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.faptebune.Domain.Orase;
import org.example.faptebune.Domain.Persoana;
import org.example.faptebune.Service.Service;

import java.util.Arrays;

public class LoginView extends VBox {
    private Service service;
    private TabPane tabPane;
    private ListView<String> usersList;

    public LoginView(Service service) {
        this.service = service;
        initializeView();
    }

    private void initializeView() {
        setPadding(new Insets(20));
        setSpacing(10);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab loginTab = createLoginTab();
        Tab registerTab = createRegisterTab();

        tabPane.getTabs().addAll(loginTab, registerTab);
        getChildren().add(tabPane);
    }

    private Tab createLoginTab() {
        Tab loginTab = new Tab("Login");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Label title = new Label("Select Username");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        usersList = new ListView<>();
        usersList.setPrefHeight(200);
        refreshUsersList();

        usersList.setOnMouseClicked(event -> {
            String selectedUsername = usersList.getSelectionModel().getSelectedItem();
            if (selectedUsername != null) {
                Persoana person = service.login(selectedUsername);
                if (person != null) {
                    redirectToMainView(person);
                }
            }
        });

        content.getChildren().addAll(title, usersList);
        loginTab.setContent(content);
        return loginTab;
    }

    private Tab createRegisterTab() {
        Tab registerTab = new Tab("Register");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        ComboBox<Orase> cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll(Orase.values());

        grid.addRow(0, new Label("Username:"), usernameField);
        grid.addRow(1, new Label("Password:"), passwordField);
        grid.addRow(2, new Label("Confirm Password:"), confirmPasswordField);
        grid.addRow(3, new Label("First Name:"), firstNameField);
        grid.addRow(4, new Label("Last Name:"), lastNameField);
        grid.addRow(5, new Label("City:"), cityComboBox);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        registerButton.setOnAction(e -> {
            if (validateRegistration(usernameField.getText(), passwordField.getText(),
                    confirmPasswordField.getText(), firstNameField.getText(),
                    lastNameField.getText(), cityComboBox.getValue())) {
                service.register(usernameField.getText(), passwordField.getText(),
                        firstNameField.getText(), lastNameField.getText(), cityComboBox.getValue());
                clearFields(new TextField[]{usernameField, passwordField, confirmPasswordField, firstNameField, lastNameField}, cityComboBox);
                refreshUsersList();
                tabPane.getSelectionModel().select(0);
            }
        });

        grid.add(registerButton, 1, 6);
        registerTab.setContent(grid);
        return registerTab;
    }

    private void redirectToMainView(Persoana person) {
        MainView mainView = new MainView(service, person);
        Scene scene = new Scene(mainView, 800, 600);
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(scene);
    }

    private void refreshUsersList() {
        String usernamesStr = service.getAllUsernames();
        usersList.getItems().clear();
        if (!usernamesStr.isEmpty()) {
            usersList.getItems().addAll(Arrays.asList(usernamesStr.split(", ")));
        }
    }

    private boolean validateRegistration(String username, String password, String confirmPassword,
                                         String firstName, String lastName, Orase city) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || city == null) {
            showAlert("All fields are required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields(TextField[] fields, ComboBox<Orase> cityComboBox) {
        for (TextField field : fields) {
            field.clear();
        }
        cityComboBox.setValue(null);
    }
}
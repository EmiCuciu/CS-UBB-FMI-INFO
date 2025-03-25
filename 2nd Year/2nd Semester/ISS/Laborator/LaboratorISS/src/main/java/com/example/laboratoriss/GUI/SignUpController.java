package com.example.laboratoriss.GUI;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Domain.UserType;
import com.example.laboratoriss.Service.IUserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignUpController {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField numeField;
    @FXML
    private TextField prenumeField;
    @FXML
    private ComboBox<UserType> userTypeComboBox;
    @FXML
    private TextField sectieField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button cancelButton;

    private IUserService userService;

    public void setService(IUserService userService) {
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        // Configure user type combo box
        userTypeComboBox.setItems(FXCollections.observableArrayList(UserType.values()));
        userTypeComboBox.getSelectionModel().selectFirst();

        // Show/hide sectie field based on user type
        sectieField.setDisable(true);
        userTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            sectieField.setDisable(newVal == UserType.FARMACIST);
        });

        // Set up button handlers
        signUpButton.setOnAction(event -> handleSignUp());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleSignUp() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        String username = usernameField.getText();
        User newUser = getUser(username);

        User registeredUser = userService.register(newUser);
        if (registeredUser != null) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful",
                    "User " + username + " has been registered successfully!");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed",
                    "Username already exists or registration failed.");
        }
    }

    private User getUser(String username) {
        String password = passwordField.getText();
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        UserType userType = userTypeComboBox.getValue();
        String sectie = sectieField.getText();

        User newUser;
        if (userType == UserType.FARMACIST) {
            newUser = new User(null, username, password, userType, nume, prenume);
        } else {
            newUser = new User(null, username, password, userType, nume, prenume, sectie);
        }
        return newUser;
    }

    private boolean validateInput() {
        // Check for empty fields
        if (usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty() ||
                numeField.getText().isEmpty() ||
                prenumeField.getText().isEmpty() ||
                (userTypeComboBox.getValue() == UserType.PERSONAL_SECTIE && sectieField.getText().isEmpty())) {

            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return false;
        }

        // Check password match
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords don't match!");
            return false;
        }

        return true;
    }

    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
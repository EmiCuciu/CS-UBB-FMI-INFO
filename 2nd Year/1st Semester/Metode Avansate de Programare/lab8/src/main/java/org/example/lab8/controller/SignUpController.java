package org.example.lab8.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.example.lab8.domain.Utilizator;

public class SignUpController {
    Controller controller = ApplicationContext.getController();

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        signUpButton.setOnAction(event -> handleSignUp());
    }

    private void handleSignUp() {
        String first = firstName.getText();
        String last = lastName.getText();
        String user = username.getText();
        String pass = password.getText();

        Utilizator utilizator = new Utilizator(first, last);
        utilizator.setUsername(user);
        utilizator.setPassword(pass);

        controller.getService().getUserService().addUtilizator(utilizator);

        System.out.println("User saved: " + utilizator);
        SceneManager.switchScene("/org/example/lab8/login.fxml");
    }
}
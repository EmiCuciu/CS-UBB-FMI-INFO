package org.example.lab6.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends ControllerSuperclass {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;





    @FXML
    protected void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        usernameField.clear();
        passwordField.clear();
        if (getUserService().login(username,password)) {
            System.out.println(username + " Logged in!");
            getScreenService().switchScene("main");
        } else{
            System.out.println("Invalid username or password!");
        }

    }
}

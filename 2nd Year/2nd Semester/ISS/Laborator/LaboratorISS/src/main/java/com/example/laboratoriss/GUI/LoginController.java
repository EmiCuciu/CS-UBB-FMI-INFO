package com.example.laboratoriss.GUI;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Domain.UserType;
import com.example.laboratoriss.Service.IComandaService;
import com.example.laboratoriss.Service.IMedicamentService;
import com.example.laboratoriss.Service.IUserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LoginController {

    private static final Logger logger = LogManager.getLogger();

    private IUserService userService;
    private IMedicamentService medicamentService;
    private IComandaService comandaService;

    public Button Login;
    @FXML
    private Button SignUp;
    @FXML
    private TextField UsernameTextField;
    @FXML
    private TextField PasswordTextField;

    public void setServices(IUserService userService, IMedicamentService medicamentService, IComandaService comandaService) {
        this.userService = userService;
        this.medicamentService = medicamentService;
        this.comandaService = comandaService;
    }

    @FXML
    public void initialize() {
        Login.setOnAction(event -> handleLogin());
        SignUp.setOnAction(event -> handleSignUp());
    }

    private void handleLogin() {
        String username = UsernameTextField.getText();
        String password = PasswordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login failed", "Please enter username and password.");
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {

            logger.info("User logged in: {}", user);
            openAppropriateWindow(user);
        } else {
            showAlert("Login failed", "Invalid username or password.");
        }
    }

    private void openAppropriateWindow(User user) {
        try {
            if (user.getType() == UserType.FARMACIST) {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        LoginController.class.getResource("/com/example/laboratoriss/farmacie.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                FarmacieController controller = fxmlLoader.getController();
                controller.setServices(medicamentService, user);

                Stage stage = new Stage();
                stage.setTitle("Pharmacy Management - " + user.getNume() + " " + user.getPrenume());
                stage.setScene(scene);
                stage.setResizable(false);

                stage.show();

                // Close login window
//                ((Stage) Login.getScene().getWindow()).close();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        LoginController.class.getResource("/com/example/laboratoriss/personalsectie.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                PersonalSectieController controller = fxmlLoader.getController();
                controller.setServices(medicamentService, comandaService, user);

                Stage stage = new Stage();
                stage.setTitle("Personal Sectie - " + user.getNume() + " " + user.getPrenume() + " sectia de " + user.getSectie());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

                // Close login window
            }
        } catch (IOException e) {
            logger.error("Error opening window", e);
            showAlert("Error", "Could not open application window.");
        }
    }

    private void handleSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    LoginController.class.getResource("/com/example/laboratoriss/signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            SignUpController controller = fxmlLoader.getController();
            controller.setService(userService);

            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            logger.error("Error opening signup window", e);
            showAlert("Error", "Could not open signup window.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
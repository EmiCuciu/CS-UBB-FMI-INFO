package com.example.laborator;

import com.example.laborator.GUI.LoginController;
import com.example.laborator.Repository.*;
import com.example.laborator.Service.AuthenticationService;
import com.example.laborator.Service.ParticipantService;
import com.example.laborator.Service.ProbaService;
import com.example.laborator.Service.RezultatService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Load database properties
            Properties props = new Properties();
            try (FileReader fr = new FileReader("bd.config")) {
                props.load(fr);
            }

            // Initialize repositories
            IArbitruRepository arbitruRepository = new ArbitruRepository(props);
            IParticipantRepository participantRepository = new ParticipantRepository(props);
            IProbaRepository probaRepository = new ProbaRepository(props, arbitruRepository);
            IRezultatRepository rezultatRepository = new RezultatRepository(props, participantRepository, arbitruRepository);

            // Initialize services
            AuthenticationService authService = new AuthenticationService(arbitruRepository);
            ParticipantService participantService = new ParticipantService(participantRepository, rezultatRepository);
            ProbaService probaService = new ProbaService(probaRepository);
            RezultatService rezultatService = new RezultatService(rezultatRepository);

            // Load login view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();

            // Configure controller
            LoginController loginController = fxmlLoader.getController();
            loginController.setAuthService(authService);
            loginController.setServices(participantService, probaService, rezultatService);

            // Show login screen
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Triatlon Competition - Login");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
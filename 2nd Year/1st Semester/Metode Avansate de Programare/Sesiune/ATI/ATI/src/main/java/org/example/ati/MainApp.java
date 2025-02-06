package org.example.ati;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ati.GUI.PacientiController;
import org.example.ati.GUI.PaturiController;
import org.example.ati.Repository.PacientRepository;
import org.example.ati.Repository.PatRepository;
import org.example.ati.Service.Service;

public class MainApp extends Application {
    private Service service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        service = new Service(new PatRepository(), new PacientRepository());

        // Load Pacienti window
        FXMLLoader pacientiLoader = new FXMLLoader(getClass().getResource("/org/example/ati/pacienti.fxml"));
        Scene pacientiScene = new Scene(pacientiLoader.load());
        Stage pacientiStage = new Stage();
        pacientiStage.setScene(pacientiScene);
        pacientiStage.setTitle("Pacienti");
        PacientiController pacientiController = pacientiLoader.getController();
        pacientiController.setService(service);
        pacientiStage.show();

        // Load Paturi window
        FXMLLoader paturiLoader = new FXMLLoader(getClass().getResource("/org/example/ati/paturi.fxml"));
        Scene paturiScene = new Scene(paturiLoader.load());
        Stage paturiStage = new Stage();
        paturiStage.setScene(paturiScene);
        paturiStage.setTitle("Paturi");
        PaturiController paturiController = paturiLoader.getController();
        paturiController.setService(service);
        paturiStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
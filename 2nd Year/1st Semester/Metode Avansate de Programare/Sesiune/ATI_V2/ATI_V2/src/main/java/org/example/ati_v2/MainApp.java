package org.example.ati_v2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ati_v2.GUI.PacientiController;
import org.example.ati_v2.GUI.PaturiController;
import org.example.ati_v2.Repository.PacientRepository;
import org.example.ati_v2.Repository.PatRepository;
import org.example.ati_v2.Service.Service;

public class MainApp extends Application {
    private Service service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        service = new Service(new PatRepository(), new PacientRepository());

        ///  LOAD PACIENTI WINDOW
        FXMLLoader pacientiLoader = new FXMLLoader(getClass().getResource("/org/example/ati_v2/pacienti.fxml"));
        Scene pacientiScene = new Scene(pacientiLoader.load());
        Stage pacientiStage = new Stage();
        pacientiStage.setResizable(false);
        pacientiStage.setScene(pacientiScene);
        pacientiStage.setTitle("Pacienti");

        PacientiController pacientiController = pacientiLoader.getController();
        pacientiController.setService(service);
        pacientiStage.show();

        /// LOAD PATURI WINDOW
        FXMLLoader paturiLoader = new FXMLLoader(getClass().getResource("/org/example/ati_v2/paturi.fxml"));
        Scene paturiScene = new Scene(paturiLoader.load());
        Stage paturiStage = new Stage();
        paturiStage.setResizable(false);
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
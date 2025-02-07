package org.example.apeleromane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.apeleromane.GUI.AvertizariController;
import org.example.apeleromane.GUI.RauController;
import org.example.apeleromane.Repository.LocalitateRepository;
import org.example.apeleromane.Repository.RauRepository;
import org.example.apeleromane.Service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    private Service service;

    @Override
    public void start(Stage stage) throws IOException {
        RauRepository rauRepository = new RauRepository();
        service = new Service(rauRepository, new LocalitateRepository(rauRepository));

        // Load Rauri window
        FXMLLoader rauriLoader = new FXMLLoader(getClass().getResource("/org/example/apeleromane/rauri.fxml"));
        Scene rauriScene = new Scene(rauriLoader.load());
        Stage rauriStage = new Stage();
        rauriStage.setScene(rauriScene);
        rauriStage.setTitle("Rauri");
        RauController rauriController = rauriLoader.getController();
        rauriController.setService(service);
        rauriStage.show();


        // Load Avertizari window
        FXMLLoader avertizariLoader = new FXMLLoader(getClass().getResource("/org/example/apeleromane/avertizari.fxml"));
        Scene avertizariScene = new Scene(avertizariLoader.load());
        Stage avertizariStage = new Stage();
        avertizariStage.setScene(avertizariScene);
        avertizariStage.setTitle("Avertizari");
        AvertizariController avertizariController = avertizariLoader.getController();
        avertizariController.setService(service);
        avertizariStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
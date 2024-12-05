package org.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lab8.controller.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/org/example/lab8/login.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/**
 *
   TODO:     4. Sa fac un sistem de notificari pentru cand cineva iti trimite un mesaj, sa iti apara un badge(un sclipitor ceva) pe butonul de messages

    TODO:   7. Sa rezolv cu repo paginat (LAB 10)
 */
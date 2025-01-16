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
        primaryStage.setTitle("Mafia Social Network");
        primaryStage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/org/example/lab8/images/money.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
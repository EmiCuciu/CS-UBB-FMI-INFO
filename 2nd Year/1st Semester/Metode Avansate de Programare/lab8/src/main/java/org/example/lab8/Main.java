package org.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lab8.controller.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lab8/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mafia Network");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/org/example/lab8/images/money.png"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
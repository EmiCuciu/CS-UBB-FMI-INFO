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
 * TODO: 1. Sa pot da reply la un mesaj, adica sa schimb areatext din messages in ceva tabela
 *          , unde sa pot da click pe un mesaj si sa am un buton reply
 *          care sa refere acel mesaj, de exemplu replyed to: "mesajul la care se raspunde"
 *          , si sa pot scrie un mesaj nou care sa fie reply la acel mesaj
 *
   TODO:     4. Sa fac un sistem de notificari pentru cand cineva iti trimite un mesaj, sa iti apara un badge(un sclipitor ceva) pe butonul de messages

    TODO:   6. Sa am posibilitatea de a trimite de o data acelasi mesaj la mai multe persoane

    TODO:   7. Sa rezolv cu repo paginat (LAB 10)
 */
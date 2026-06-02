package org.example.grile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


/** TODO:
 1: O fereastra pentru angajatii restaurantului , NUMELE "Staff"
 2: Cate o fereastra pentru fiecare masa citita din fisierul/tabelul Tables
 3: FEREASTRA ASOCIATA MESEI VA AVEA NUMELE  ex: "Table 3"
 4: Fiecare fereastra a meselor va afisa meniul restaurantului grupat pe categorii,
    acesta fiind citit din fisierul/tabelul Menu



 */
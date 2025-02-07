package org.example.zboruri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.zboruri.Domain.Client;
import org.example.zboruri.GUI.MainController;

import java.io.IOException;

public class ZboruriApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ZboruriApplication.class.getResource("/org/example/zboruri/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flight Booking - Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openMainInterface(Client client) throws IOException {
        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ZboruriApplication.class.getResource("/org/example/zboruri/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        MainController mainController = fxmlLoader.getController();
        mainController.setLoggedInUser(client);
        mainStage.setTitle("Flight Booking - Main");
        mainStage.setResizable(true);
        mainStage.setScene(scene);
        mainStage.show();
    }
}
package org.example.lab8.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is used to switch between scenes.
 */
public class SceneManager {
    private static Stage primaryStage;
    private static final String LOGIN_FXML = "/org/example/lab8/login.fxml";

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile) {
        if (isLoginScene() && fxmlFile.equals(LOGIN_FXML)) {
            return; // Do not switch if the current scene is already the login scene
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML file: " + fxmlFile);
        }
    }

    public static void openNewWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Mafia Social Network");
            newStage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/org/example/lab8/images/money.png")));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML file: " + fxmlFile);
        }
    }

    private static boolean isLoginScene() {
        return primaryStage.getScene() != null && primaryStage.getScene().getRoot().getUserData() != null
                && primaryStage.getScene().getRoot().getUserData().equals("login");
    }
}
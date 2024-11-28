package com.example.guiex1;

import com.example.guiex1.controller.LoginController;
import com.example.guiex1.repository.dbrepo.PrietenieDBRepository;
import com.example.guiex1.repository.dbrepo.UserDBRepository;
import com.example.guiex1.services.Service;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guiex1/views/login.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        LoginController loginController = loader.getController();
        loginController.setService(new Service(
                new UserDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", new UtilizatorValidator()),
                new PrietenieDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", new PrietenieValidator())
        ));
        loginController.setPrimaryStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package org.example.faptebune;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.faptebune.Domain.Nevoie;
import org.example.faptebune.Domain.Persoana;
import org.example.faptebune.GUI.LoginView;
import org.example.faptebune.Repository.IRepository;
import org.example.faptebune.Repository.NevoieRepository;
import org.example.faptebune.Repository.PersoanaRepository;
import org.example.faptebune.Service.Service;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        IRepository<Long, Persoana> personRepo = new PersoanaRepository();
        IRepository<Long, Nevoie> needRepo = new NevoieRepository();
        Service service = new Service(personRepo, needRepo);

        LoginView loginView = new LoginView(service);
        Scene scene = new Scene(loginView, 800, 600);

        primaryStage.setTitle("Help Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
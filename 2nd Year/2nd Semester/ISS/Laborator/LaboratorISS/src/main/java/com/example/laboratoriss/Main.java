package com.example.laboratoriss;

import com.example.laboratoriss.GUI.FarmacieController;
import com.example.laboratoriss.GUI.LoginController;
import com.example.laboratoriss.Repository.*;
import com.example.laboratoriss.Service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting application...");

        // Load database properties
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            logger.error("Cannot find bd.config", e);
            System.exit(1);
        }

//        // Create repositories
//        UserRepository userRepository = new UserRepository(props);
//        MedicamentRepository medicamentRepository = new MedicamentRepository(props);
//        ComandaItemRepository comandaItemRepository = new ComandaItemRepository(props, medicamentRepository);
//        IComandaRepository comandaRepository = new ComandaRepository(props, userRepository, comandaItemRepository);
//
//        // Create services
//        IUserService userService = new UserService(userRepository);
//        IMedicamentService medicamentService = new MedicamentService(medicamentRepository);
//        IComandaService comandaService = new ComandaService(comandaRepository);

//         Create repositories using Hibernate
        IMedicamentRepository medicamentRepository = new com.example.laboratoriss.Repository.Hibernate.MedicamentRepository();
        IUserRepository userRepository = new com.example.laboratoriss.Repository.Hibernate.UserRepository();
        IComandaItemRepository comandaItemRepository = new com.example.laboratoriss.Repository.Hibernate.ComandaItemRepository();
        IComandaRepository comandaRepository = new com.example.laboratoriss.Repository.Hibernate.ComandaRepository();

//        Create services with Hibernate repositories
        IUserService userService = new UserService(userRepository);
        IMedicamentService medicamentService = new MedicamentService(medicamentRepository);
        IComandaService comandaService = new ComandaService(comandaRepository);

        // Store comandaService in a static context for later access by controllers
        FarmacieController.ApplicationContext.setComandaService(comandaService);

        // Load the login screen
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Get the controller and inject services
        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(userService, medicamentService, comandaService);

        // Set up the stage
        stage.setTitle("Pharmacy Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        logger.info("Application started successfully");
    }

    public static void main(String[] args) {
        launch();
    }
}
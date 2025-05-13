package com.example.laboratoriss;

import com.example.laboratoriss.GUI.FarmacieController;
import com.example.laboratoriss.GUI.LoginController;
import com.example.laboratoriss.Repository.Hibernate.HibernateComandaItemRepository;
import com.example.laboratoriss.Repository.Hibernate.HibernateComandaRepository;
import com.example.laboratoriss.Repository.Hibernate.HibernateMedicamentRepository;
import com.example.laboratoriss.Repository.Hibernate.HibernateUserRepository;
import com.example.laboratoriss.Repository.IComandaItemRepository;
import com.example.laboratoriss.Repository.IComandaRepository;
import com.example.laboratoriss.Repository.IMedicamentRepository;
import com.example.laboratoriss.Repository.IUserRepository;
import com.example.laboratoriss.Service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting application...");

        // Load database properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("iss.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Cannot find iss.properties in classpath");
            }
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load application properties", e);
        }

//        // Create repositories
//        UserRepository userRepository = new UserRepository(props);
//        MedicamentRepository medicamentRepository = new MedicamentRepository(props);
//        ComandaItemRepository comandaItemRepository = new ComandaItemRepository(props, medicamentRepository);
//        IComandaRepository comandaRepository = new ComandaRepository(props, userRepository, comandaItemRepository);
//

//         Create repositories using Hibernate
        IMedicamentRepository medicamentRepository = new HibernateMedicamentRepository(props);
        IUserRepository userRepository = new HibernateUserRepository(props);
        IComandaItemRepository comandaItemRepository = new HibernateComandaItemRepository(props);
        IComandaRepository comandaRepository = new HibernateComandaRepository(props);

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
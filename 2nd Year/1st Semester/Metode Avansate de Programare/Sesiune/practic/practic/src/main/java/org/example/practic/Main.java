package org.example.practic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.practic.Domain.Driver;
import org.example.practic.GUI.DispatcherWindowController;
import org.example.practic.GUI.DriverWindowController;
import org.example.practic.Repository.DriverRepository;
import org.example.practic.Repository.OrderRepository;
import org.example.practic.Service.TaxiService;

public class Main extends Application {
    private TaxiService service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize repositories and service
        DriverRepository driverRepository = new DriverRepository();
        OrderRepository orderRepository = new OrderRepository();
        service = new TaxiService(driverRepository, orderRepository);

        // Create dispatcher window
        FXMLLoader dispatcherLoader = new FXMLLoader(getClass().getResource("/org/example/practic/DispatcherWindow.fxml"));
        Stage dispatcherStage = new Stage();
        dispatcherStage.setScene(new Scene(dispatcherLoader.load()));
        dispatcherStage.setTitle("Taxi Dispatcher");
        dispatcherStage.setResizable(false);
        DispatcherWindowController dispatcherController = dispatcherLoader.getController();
        dispatcherController.initialize(service);
        dispatcherStage.show();

        // Create windows for each driver
        for (Driver driver : service.getAllDrivers()) {
            FXMLLoader driverLoader = new FXMLLoader(getClass().getResource("/org/example/practic/DriverWindow.fxml"));
            Stage driverStage = new Stage();
            driverStage.setScene(new Scene(driverLoader.load()));
            driverStage.setTitle("Driver: " + driver.getName());
            driverStage.setResizable(false);
            DriverWindowController driverController = driverLoader.getController();
            driverController.initialize(driver, service);
            driverStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.example.lab4;

import com.example.lab4.ctrl.ComputerRepairShopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.example.lab4.repository.ComputerRepairRequestRepository;
import com.example.lab4.repository.file.ComputerRepairRequestFileRepository;
import com.example.lab4.repository.file.ComputerRepairedFormFileRepository;
import com.example.lab4.services.ComputerRepairServices;
import com.example.lab4.services.ServicesException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RepairShopWindow.fxml"));
            Parent root = loader.load();
            ComputerRepairShopController ctrl = loader.getController();
            ctrl.setService(getService());
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Computer Repairs Shop");
            primaryStage.show();
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    static ComputerRepairServices getService() throws ServicesException {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("RepairShop.properties"));
            String requestFileName=properties.getProperty("RequestsFile");
            if (requestFileName==null){
                requestFileName="ComputerRequests.txt";
                System.err.println("Requests file not found. Using default "+requestFileName);
            }
            String formsFileName=properties.getProperty("RepairedFile");
            if (formsFileName==null){
                formsFileName="RepairedForms.txt";
                System.err.println("RepairedForms file not found. Using default "+formsFileName);
            }
            ComputerRepairRequestRepository crrRepo = new ComputerRepairRequestFileRepository(requestFileName);
            ComputerRepairedFormFileRepository crfRepo = new ComputerRepairedFormFileRepository(formsFileName, crrRepo);
            return new ComputerRepairServices(crrRepo, crfRepo);
        }catch (IOException ex){
            throw new ServicesException("Error starting app "+ex);
        }
    }
}

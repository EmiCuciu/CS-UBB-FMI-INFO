package org.example.grile.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.grile.Domain.Table;
import org.example.grile.Service.Service;

import java.io.IOException;
import java.util.List;

public class GUI extends Application {
    private final Service service = ApplicationContext.getService();

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load and show the staff window
        FXMLLoader staffLoader = new FXMLLoader(getClass().getResource("/org/example/grile/Staff.fxml"));
        Scene staffScene = new Scene(staffLoader.load());
        Stage staffStage = new Stage();
        staffStage.setTitle("Staff");
        staffStage.setScene(staffScene);
        staffStage.show();

        // Load and show the table windows
        List<Table> tables = service.getAllTables();
        for (Table table : tables) {
            FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("/org/example/grile/Mese.fxml"));
            Scene tableScene = new Scene(tableLoader.load());
            Mese meseController = tableLoader.getController();
            meseController.setTableId(table.getId());

            Stage tableStage = new Stage();
            tableStage.setTitle("Table " + table.getId());
            tableStage.setScene(tableScene);
            tableStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
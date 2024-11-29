package org.example.exercitiu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Repository repository = new Repository();
    Service service = new Service(repository);

    @Override
    public void start(Stage stage) throws IOException {
        service.addPredefinite();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setService(service);

        Scene scene = new Scene(root);
        stage.setTitle("Tabel de teme");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
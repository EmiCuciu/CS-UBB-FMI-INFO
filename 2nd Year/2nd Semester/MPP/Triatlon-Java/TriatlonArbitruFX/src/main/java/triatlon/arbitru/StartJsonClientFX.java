package triatlon.arbitru;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.arbitru.gui.LoginController;
import triatlon.network.jsonprotocol.TriatlonServicesJsonProxy;
import triatlon.services.ITriatlonServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application {
    private static final Logger logger = LogManager.getLogger(StartJsonClientFX.class);
    private Stage primaryStage;
    private static final int defaultPort = 55556;
    private static final String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/chatclient.properties"));
            logger.info("Client properties loaded");
        } catch (IOException e) {
            logger.error("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number " + ex.getMessage());
            serverPort = defaultPort;
        }
        logger.info("Using server IP {}, port {}", serverIP, serverPort);
        ITriatlonServices server = new TriatlonServicesJsonProxy(serverIP, serverPort);

        // Load login view
        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root = loginLoader.load();

        // Get login controller and set the server
        LoginController loginController = loginLoader.getController();
        loginController.setServer(server);

        // Set up primary stage
        primaryStage.setTitle("Triatlon Competition - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
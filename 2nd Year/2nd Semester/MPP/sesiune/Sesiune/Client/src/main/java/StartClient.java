import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gui.MainController;
import gui.LoginController;
import network.object_protocol.ServicesProxy;
import services.IServices;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class StartClient extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartClient.class);


    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.debug("In start...");
        Properties props = new Properties();
        try {
            props.load(StartClient.class.getResourceAsStream("/client.properties"));
            logger.info("Client properties set {}", props);
        } catch (IOException e) {
            logger.error("Cannot find client.properties " + e);
            logger.debug("Looking into folder {}", (new File(".").getAbsolutePath()));
            return;
        }
        String serverIP = props.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException e) {
            logger.error("Wrong port number " + e.getMessage());
            logger.debug("Using default port: " + defaultPort);
        }

        logger.info("Using server IP: " + serverIP);
        logger.info("Using server port: " + serverPort);

        IServices server = new ServicesProxy(serverIP, serverPort);

        System.out.println("login-interface.fxml resource: " + getClass().getClassLoader().getResource("login-interface.fxml"));
        System.out.println("main-interface.fxml resource: " + getClass().getClassLoader().getResource("main-interface.fxml"));



        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login-interface.fxml"));
        Parent root = loader.load();
        LoginController ctrl = loader.getController();
        ctrl.setServer(server);

        FXMLLoader mloader = new FXMLLoader(getClass().getClassLoader().getResource("main-interface.fxml"));
        Parent mroot = mloader.load();
        MainController actrl = mloader.getController();
        actrl.setServer(server);

        ctrl.setJucatorPageController(actrl);
        ctrl.setParent(mroot);

        primaryStage.setTitle("LogIn");
        primaryStage.setScene(new Scene(root, 700, 350));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

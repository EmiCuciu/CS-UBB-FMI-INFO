package chat.client;

import chat.client.gui.ChatController;
import chat.client.gui.LoginController;
import chat.network.objectprotocol.ChatServicesObjectProxy;
import chat.services.IChatServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartObjectClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartRpcClientFX.class);

    public void start(Stage primaryStage) throws Exception {
        logger.debug("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/chatclient.properties"));
            logger.info("Client properties set {} ", clientProps);
            // clientProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find chatclient.properties " + e);
            logger.debug("Looking into folder {}", (new File(".")).getAbsolutePath());
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultChatPort);
        }
        logger.info("Using server IP " + serverIP);
        logger.info("Using server port " + serverPort);


        IChatServices server = new ChatServicesObjectProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("LoginW.fxml"));
        Parent root=loader.load();
        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("ChatW.fxml"));
        Parent croot=cloader.load();
        ChatController chatCtrl =
                cloader.<ChatController>getController();
        chatCtrl.setServer(server);

        ctrl.setChatController(chatCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root, 300, 130));
        primaryStage.show();
    }
}

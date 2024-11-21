package org.example.lab6.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lab6.Controllers.ControllerSuperclass;
import org.example.lab6.HelloApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenService  {
    private final Stage primaryStage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, ControllerSuperclass> controllers = new HashMap<>();
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final FriendshipRequestService friendshipRequestService;


    public ScreenService(Stage primaryStage, UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService) {
        this.primaryStage = primaryStage;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
    }

    public void addScene(String name, String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        try {
            Parent root = loader.load();
            ControllerSuperclass controller = loader.getController();
            controller.setUserService(userService);
            controller.setScreenService(this);
            controller.setFriendshipService(friendshipService);
            controller.setFriendshipRequestService(friendshipRequestService);
            scenes.put(name, new Scene(root));
            controllers.put(name, controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchScene(String name) {
        Scene scene = scenes.get(name);
        ControllerSuperclass controller = controllers.get(name);
        if (scene != null) {
            controller.init();
            primaryStage.setScene(scene);
        } else {
            System.out.println("Scene not found: " + name);
        }
    }
}

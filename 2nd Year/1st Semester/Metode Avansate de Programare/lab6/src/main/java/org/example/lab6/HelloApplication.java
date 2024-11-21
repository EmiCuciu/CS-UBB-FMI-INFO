package org.example.lab6;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.lab6.domain.*;
import org.example.lab6.domain.validation.FriendshipRequestValidator;
import org.example.lab6.domain.validation.FriendshipValidator;
import org.example.lab6.domain.validation.UserValidation;
import org.example.lab6.repository.Repository;
import org.example.lab6.repository.database.FriendshipDBRepository;
import org.example.lab6.repository.database.FriendshipRequestDBRepository;
import org.example.lab6.repository.database.UserDBRepository;
import org.example.lab6.service.FriendshipRequestService;
import org.example.lab6.service.FriendshipService;
import org.example.lab6.service.ScreenService;
import org.example.lab6.service.UserService;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Repository<Long, User> userRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "emi12345" ,new UserValidation());
        Repository<Tuple<Long, Long>, Friendship> friendRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "emi12345" ,new FriendshipValidator());
        Repository<Tuple<Long, Long>, FriendshipRequest> friendshipRequestRepository = new FriendshipRequestDBRepository("jdbc:postgresql://localhost:5432/social_network", "postgres", "emi12345" ,new FriendshipRequestValidator());

        UserService userService = UserService.getInstance();
        userService.setRepository(userRepository);
        FriendshipService friendshipService = FriendshipService.getInstance();
        friendshipService.setRepo(friendRepository);
        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestRepository, friendshipService);

        UserInstance.getInstance();

        ScreenService screenService = new ScreenService(stage, userService, friendshipService, friendshipRequestService);

        screenService.addScene("login", "/org/example/lab6/Views/login.fxml");
        screenService.addScene("main", "/org/example/lab6/Views/mainWindow.fxml");
        screenService.addScene("friendshipRequests", "/org/example/lab6/Views/friendRequestWindow.fxml");
        screenService.switchScene("login");

        stage.setWidth(800);
        stage.setHeight(600);

        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
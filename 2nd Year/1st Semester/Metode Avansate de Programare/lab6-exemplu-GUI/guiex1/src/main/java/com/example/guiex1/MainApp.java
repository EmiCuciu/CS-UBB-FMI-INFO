package com.example.guiex1;

import com.example.guiex1.controller.Controller;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.dbrepo.PrietenieDBRepository;
import com.example.guiex1.repository.dbrepo.UserDBRepository;
import com.example.guiex1.services.Service;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    private Service service;
    private Controller controller;
    private Utilizator loggedInUser;
    private ObservableList<Utilizator> friendsObservableList;

    public void setUser(Utilizator user) {
        this.loggedInUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
        PrietenieValidator prietenieValidator = new PrietenieValidator();
        service = new Service(new UserDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", utilizatorValidator), new PrietenieDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", prietenieValidator));
        controller = new Controller();
        controller.setService(service);

        Label userIdLabel = new Label(loggedInUser.getId() + " " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

        TableView<Utilizator> userTable = new TableView<>();
        TableColumn<Utilizator, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Utilizator, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));


        userTable.getColumns().addAll(firstNameCol, lastNameCol);



        friendsObservableList = FXCollections.observableArrayList();
        userTable.setItems(friendsObservableList);
        refreshFriendsList();

        Button addFriendButton = new Button("Add Friend");
        addFriendButton.setOnAction(e -> showAddFriendDialog(loggedInUser));

        Button removeFriendButton = new Button("Remove Friend");
        removeFriendButton.setOnAction(e -> {
            Utilizator selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                controller.removeFriend(loggedInUser.getId(), selectedUser.getId());
                refreshFriendsList();
            }
        });

        Button friendRequestsButton = new Button("Friend Requests");
        friendRequestsButton.setOnAction(e -> showFriendRequests(loggedInUser));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshFriendsList());

        Button loginAsAnotherUserButton = new Button("Login as Another User");
        loginAsAnotherUserButton.setOnAction(e -> showLoginDialog());

        VBox vbox = new VBox(userIdLabel, userTable, addFriendButton, removeFriendButton, friendRequestsButton, refreshButton, loginAsAnotherUserButton);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshFriendsList() {
        List<Utilizator> friendsList = new ArrayList<>(controller.findFriends(loggedInUser.getId()));
        friendsObservableList.setAll(friendsList);
    }

    private void showAddFriendDialog(Utilizator user) {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name");

        TableView<Utilizator> allUsersTable = new TableView<>();
        TableColumn<Utilizator, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Utilizator, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        allUsersTable.getColumns().addAll(firstNameCol, lastNameCol);

        List<Utilizator> allUsersList = new ArrayList<>();
        service.getAll().forEach(allUsersList::add);
        allUsersList.removeAll(controller.findFriends(user.getId()));
        ObservableList<Utilizator> allUsersObservableList = FXCollections.observableArrayList(allUsersList);
        allUsersTable.setItems(allUsersObservableList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Utilizator> filteredList = allUsersList.stream()
                    .filter(u -> u.getFirstName().toLowerCase().contains(newValue.toLowerCase()) ||
                            u.getLastName().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
            allUsersObservableList.setAll(filteredList);
        });

        Button addButton = new Button("Add Selected Friend");
        addButton.setOnAction(e -> {
            Utilizator selectedUser = allUsersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                controller.addFriendRequest(user.getId(), selectedUser.getId());
                allUsersTable.getItems().remove(selectedUser);
            }
        });

        vbox.getChildren().addAll(allUsersTable, searchField, addButton);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void showFriendRequests(Utilizator user) {
        Stage stage = new Stage();
        TableView<Prietenie> requestsTable = new TableView<>();
        TableColumn<Prietenie, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Prietenie, LocalDateTime> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        requestsTable.getColumns().addAll(statusCol, dateCol);

        List<Prietenie> requestsList = new ArrayList<>(controller.findFriendRequests(user.getId()));
        ObservableList<Prietenie> requestsObservableList = FXCollections.observableArrayList(requestsList);
        requestsTable.setItems(requestsObservableList);

        Button acceptButton = new Button("Accept Selected Request");
        acceptButton.setOnAction(e -> {
            Prietenie selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                controller.acceptFriendRequest(user.getId(), selectedRequest.getId().getE1());
                requestsTable.getItems().remove(selectedRequest);
                refreshFriendsList();
            }
        });

        VBox vbox = new VBox(requestsTable, acceptButton);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void showLoginDialog() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            Utilizator user = authenticate(username, password);
            if (user != null) {
                openNewUserStage(user);
                stage.close();
            } else {
                // Show error message
                System.out.println("Invalid username or password");
            }
        });

        signupButton.setOnAction(e -> showSignupDialog());

        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signupButton);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void showSignupDialog() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        Button signupButton = new Button("Sign Up");

        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            Utilizator newUser = new Utilizator(firstName, lastName);
            newUser.setUsername(username);
            newUser.setPassword(password);
            service.saveUser(newUser);
            stage.close();
        });

        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, firstNameLabel, firstNameField, lastNameLabel, lastNameField, signupButton);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private Utilizator authenticate(String username, String password) {
        return service.findByUsernameAndPassword(username, password).orElse(null);
    }

    private void openNewUserStage(Utilizator user) {
        Stage newStage = new Stage();
        Label userIdLabel = new Label(user.getId() + " " + user.getFirstName() + " " + user.getLastName());

        TableView<Utilizator> userTable = new TableView<>();
        TableColumn<Utilizator, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Utilizator, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        userTable.getColumns().addAll(firstNameCol, lastNameCol);

        ObservableList<Utilizator> friendsObservableList = FXCollections.observableArrayList();
        userTable.setItems(friendsObservableList);
        refreshFriendsListForUser(user, friendsObservableList);

        Button addFriendButton = new Button("Add Friend");
        addFriendButton.setOnAction(e -> showAddFriendDialog(user));

        Button removeFriendButton = new Button("Remove Friend");
        removeFriendButton.setOnAction(e -> {
            Utilizator selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                controller.removeFriend(user.getId(), selectedUser.getId());
                refreshFriendsListForUser(user, friendsObservableList);
            }
        });

        Button friendRequestsButton = new Button("Friend Requests");
        friendRequestsButton.setOnAction(e -> showFriendRequests(user));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshFriendsListForUser(user, friendsObservableList));

        Button loginAsAnotherUserButton = new Button("Login as Another User");
        loginAsAnotherUserButton.setOnAction(e -> showLoginDialog());

        VBox vbox = new VBox(userIdLabel, userTable, addFriendButton, removeFriendButton, friendRequestsButton, refreshButton, loginAsAnotherUserButton);
        Scene scene = new Scene(vbox);
        newStage.setScene(scene);
        newStage.show();

    }

    private void refreshFriendsListForUser(Utilizator user, ObservableList<Utilizator> friendsObservableList) {
        List<Utilizator> friendsList = new ArrayList<>(controller.findFriends(user.getId()));
        friendsObservableList.setAll(friendsList);
    }
}
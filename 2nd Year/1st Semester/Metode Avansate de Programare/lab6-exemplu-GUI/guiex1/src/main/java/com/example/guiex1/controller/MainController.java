package com.example.guiex1.controller;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label userIdLabel;
    @FXML
    private TableView<Utilizator> userTable;
    @FXML
    private TableColumn<Utilizator, String> firstNameCol;
    @FXML
    private TableColumn<Utilizator, String> lastNameCol;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button removeFriendButton;
    @FXML
    private Button friendRequestsButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button loginAsAnotherUserButton;

    private Service service;
    private Utilizator user;
    private ObservableList<Utilizator> friendsObservableList;

    public void setService(Service service) {
        this.service = service;
    }

    public void setUser(Utilizator user) {
        this.user = user;
        userIdLabel.setText("User ID: " + user.getId());
        loadUserData();
    }

    private void loadUserData() {
        friendsObservableList = FXCollections.observableArrayList(service.findFriends(user.getId()));
        userTable.setItems(friendsObservableList);
    }

    @FXML
    private void handleAddFriend() {
        showAddFriendDialog();
    }

    @FXML
    private void handleRemoveFriend() {
        Utilizator selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            service.removeFriend(user.getId(), selectedUser.getId());
            refreshFriendsList();
        }
    }

    @FXML
    private void handleFriendRequests() {
        showFriendRequests();
    }

    @FXML
    private void handleRefresh() {
        refreshFriendsList();
    }

    @FXML
    private void handleLoginAsAnotherUser() {
        showLoginDialog();
    }

    private void refreshFriendsList() {
        friendsObservableList.setAll(service.findFriends(user.getId()));
    }

    private void showAddFriendDialog() {
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

        List<Utilizator> allUsersList = new ArrayList<>(service.findFriends(user.getId()));
        allUsersList.removeAll(service.findFriends(user.getId()));
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
                service.addFriendRequest(user.getId(), selectedUser.getId());
                allUsersTable.getItems().remove(selectedUser);
            }
        });

        vbox.getChildren().addAll(allUsersTable, searchField, addButton);
        stage.setScene(new Scene(vbox));
        stage.show();
    }

    private void showFriendRequests() {
        Stage stage = new Stage();
        TableView<Prietenie> requestsTable = new TableView<>();
        TableColumn<Prietenie, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Prietenie, LocalDateTime> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        requestsTable.getColumns().addAll(statusCol, dateCol);

        List<Prietenie> requestsList = new ArrayList<>(service.findFriendRequests(user.getId()));
        ObservableList<Prietenie> requestsObservableList = FXCollections.observableArrayList(requestsList);
        requestsTable.setItems(requestsObservableList);

        Button acceptButton = new Button("Accept Selected Request");
        acceptButton.setOnAction(e -> {
            Prietenie selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                service.acceptFriendRequest(user.getId(), selectedRequest.getId().getE1());
                requestsTable.getItems().remove(selectedRequest);
                refreshFriendsList();
            }
        });

        VBox vbox = new VBox(requestsTable, acceptButton);
        stage.setScene(new Scene(vbox));
        stage.show();
    }

    private void showLoginDialog() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            Utilizator user = service.findByUsernameAndPassword(username, password).orElse(null);
            if (user != null) {
                setUser(user);
                stage.close();
            } else {
                System.out.println("Invalid username or password");
            }
        });

        Button signupButton = new Button("Sign Up");
        signupButton.setOnAction(e -> {
            showSignupDialog();
        });

        vbox.getChildren().addAll(usernameField, passwordField, loginButton, signupButton);
        stage.setScene(new Scene(vbox));
        stage.show();
    }

    private void showSignupDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guiex1/views/signup.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.initModality(Modality.APPLICATION_MODAL);

            SignupController controller = loader.getController();
            controller.setService(service);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
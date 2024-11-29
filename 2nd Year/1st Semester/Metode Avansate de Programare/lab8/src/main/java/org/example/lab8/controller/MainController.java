package org.example.lab8.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.Utilizator;

import java.util.List;


public class MainController {
    private Controller controller = ApplicationContext.getController();
    private Utilizator loggedInUser = LoginController.logedInUser;

    @FXML
    private TabPane tabPane;

    @FXML
    private Text username;


    // ALL USERS
    @FXML
    private TableView<Utilizator> usersTableAll;
    @FXML
    public TableColumn<Utilizator, String> firstNameColumn;
    @FXML
    public TableColumn<Utilizator, String> lastNameColumn;
    @FXML
    public TextField SearchUserAll;
    @FXML
    public Button SendFriendRequest;


    // FRIENDS
    @FXML
    private TableView<Utilizator> usersTableFriends;
    @FXML
    private TableColumn<Utilizator, String> fullNameColumnFriends;
    @FXML
    private Button DeleteFriend;


    // FRIEND REQUEST
    @FXML
    public TableView<Prietenie> FriendRequestTable;
    @FXML
    public TableColumn<Prietenie, String> fullNameColumnFriendRequest;
    @FXML
    public TableColumn<Prietenie, String> statusColumnFriendRequest;
    @FXML
    public TableColumn<Prietenie, String> dateColumnFriendRequest;
    @FXML
    public Button AcceptFriendRequest;


    private ObservableList<Utilizator> allUsersList;
    private ObservableList<Utilizator> friendsList;
    private ObservableList<Prietenie> friendRequestsList;

    @FXML
    public void initialize() {
        // Set the username text
        username.setText(LoginController.logedInUser.getFirstName() + " " + LoginController.logedInUser.getLastName());


        // Initialize lists
        allUsersList = getAllUsers();
        friendsList = getAllFriends();
        friendRequestsList = getAllFriendRequest();


        // Filter out friends from allUsersList
        List<Long> friendIds = friendsList.stream().map(Utilizator::getId).toList();
        allUsersList.removeIf(user -> user.getId().equals(loggedInUser.getId()) || friendIds.contains(user.getId()));


        // Tabela cu toti Userii
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usersTableAll.setItems(allUsersList);


        // Tabela cu toti Prietenii
        fullNameColumnFriends.setCellValueFactory(cellData -> {
            Utilizator user = cellData.getValue();
            return new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
        });
        usersTableFriends.setItems(friendsList);


        // Add listener for SearchUser Field & SEND FRIEND REQUEST
        SearchUserAll.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Utilizator> filteredList = allUsersList.stream()
                    .filter(u -> u.getFirstName().toLowerCase().contains(newValue.toLowerCase()) ||
                            u.getLastName().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
            usersTableAll.setItems(FXCollections.observableArrayList(filteredList));
        });
        SendFriendRequest.setOnAction(e -> {
            Utilizator selectedUser = usersTableAll.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                controller.addFriendRequest(loggedInUser.getId(), selectedUser.getId());
                allUsersList.remove(selectedUser);
            } else {
                System.out.println("No user selected.");
            }
        });


        // FriendRequests
        fullNameColumnFriendRequest.setCellValueFactory(cellData -> {
            Long senderID = cellData.getValue().getId().getE1();
            Utilizator sender = controller.getService().findUserById(senderID).orElse(null);
            if (sender == null) {
                return new SimpleStringProperty("Unknown User");
            }
            return new SimpleStringProperty(sender.getFirstName() + " " + sender.getLastName());
        });
        statusColumnFriendRequest.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumnFriendRequest.setCellValueFactory(new PropertyValueFactory<>("date"));
        FriendRequestTable.setItems(friendRequestsList);

        AcceptFriendRequest.setOnAction(e -> {
            Prietenie selectedFriendRequest = FriendRequestTable.getSelectionModel().getSelectedItem();
            if (selectedFriendRequest != null) {
                controller.acceptFriendRequest(loggedInUser.getId(), selectedFriendRequest.getId().getE1());
                friendRequestsList.remove(selectedFriendRequest);
                friendsList.setAll(getAllFriends());
            }
        });

        DeleteFriend.setOnAction(e -> {
            Utilizator selectedFriend = usersTableFriends.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                controller.removeFriend(loggedInUser.getId(), selectedFriend.getId());
                friendsList.remove(selectedFriend);
            }
        });


        // Add listeners to update tables
        friendRequestsList.addListener((ListChangeListener<Prietenie>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    friendsList.setAll(getAllFriends());
                    List<Long> updatedFriendIds = friendsList.stream().map(Utilizator::getId).toList();
                    allUsersList.setAll(getAllUsers());
                    allUsersList.removeIf(user -> user.getId().equals(loggedInUser.getId()) || updatedFriendIds.contains(user.getId()));
                }
            }
        });

        friendsList.addListener((ListChangeListener<Utilizator>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    List<Long> updatedFriendIds = friendsList.stream().map(Utilizator::getId).toList();
                    allUsersList.setAll(getAllUsers());
                    allUsersList.removeIf(user -> user.getId().equals(loggedInUser.getId()) || updatedFriendIds.contains(user.getId()));
                }
            }
        });
    }

    private ObservableList<Utilizator> getAllFriends() {
        return FXCollections.observableArrayList(controller.getService().findFriends(loggedInUser.getId()));
    }

    private ObservableList<Utilizator> getAllUsers() {
        return FXCollections.observableArrayList(controller.getService().findAllUsers());
    }

    private ObservableList<Prietenie> getAllFriendRequest() {
        return FXCollections.observableArrayList(controller.findFriendRequests(loggedInUser.getId()));
    }

    @FXML
    private void handleLogOut() {
        SceneManager.switchScene("/org/example/lab8/login.fxml");
    }

    @FXML
    private void handleDeleteAccount() {
        Utilizator utilizator = controller.getService().findUtilizatorByUsername(LoginController.logedInUsername);
        controller.getService().deleteUtilizator(utilizator.getId());
        SceneManager.switchScene("/org/example/lab8/login.fxml");
    }

    public Utilizator getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Utilizator loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
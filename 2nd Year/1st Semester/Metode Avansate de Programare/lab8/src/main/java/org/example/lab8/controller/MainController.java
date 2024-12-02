package org.example.lab8.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.lab8.domain.Message;
import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.Utilizator;
import org.example.lab8.utils.events.Event;
import org.example.lab8.utils.observer.Observer;
import org.example.lab8.services.Service;
import org.example.lab8.services.UserService;
import org.example.lab8.services.FriendshipService;
import org.example.lab8.services.MessageService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainController implements Observer {
    private Service mainService = ApplicationContext.getService();
    private UserService userService = mainService.getUserService();
    private FriendshipService friendshipService = mainService.getFriendshipService();
    private MessageService messageService = mainService.getMessageService();
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

    // Message
    @FXML
    private TextField MessageTextField;
    @FXML
    private Button SendMessageButton;
    @FXML
    private TableView<Utilizator> messageTableView;
    @FXML
    private TableColumn<Utilizator, String> messageTableColumn;
    @FXML
    private TextArea messageTextArea;

    // Notificari
    @FXML
    private Label notificationLabel;
    @FXML
    private Button ShowNotificationButton;

    private ObservableList<Utilizator> allUsersList;
    private ObservableList<Utilizator> friendsList;
    private ObservableList<Prietenie> friendRequestsList;

    @FXML
    public void initialize() {
        // Set the username text
        username.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

        // Initialize lists
        allUsersList = FXCollections.observableArrayList();
        friendsList = FXCollections.observableArrayList();
        friendRequestsList = FXCollections.observableArrayList();

        // Load initial data
        loadAllUsers();
        loadFriends();
        loadFriendRequests();

        // Set up table columns
        setupTableColumns();

        // Add listeners
        addListeners();

        // Register observer
        mainService.getUserService().addObserver(this);
        mainService.getFriendshipService().addObserver(this);
        mainService.getMessageService().addObserver(this);
    }

    private void loadAllUsers() {
        HashSet<Utilizator> users = userService.findAllUsers();
        allUsersList.setAll(users);
        filterAllUsers();
    }

    private void loadFriends() {
        Set<Utilizator> friends = friendshipService.findFriends(loggedInUser.getId());
        friendsList.setAll(friends);
    }

    private void loadFriendRequests() {
        Set<Prietenie> requests = friendshipService.findFriendRequests(loggedInUser.getId());
        friendRequestsList.setAll(requests);
    }

    private void filterAllUsers() {
        List<Long> friendIds = friendsList.stream().map(Utilizator::getId).toList();
        allUsersList.removeIf(user -> user.getId().equals(loggedInUser.getId()) || friendIds.contains(user.getId()));
    }

    private void setupTableColumns() {
        // All Users Table
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usersTableAll.setItems(allUsersList);

        // Friends Table
        fullNameColumnFriends.setCellValueFactory(cellData -> {
            Utilizator user = cellData.getValue();
            return new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
        });
        usersTableFriends.setItems(friendsList);

        // Friend Requests Table
        fullNameColumnFriendRequest.setCellValueFactory(cellData -> {
            Long senderID = cellData.getValue().getId().getE1();
            Utilizator sender = userService.findUserById(senderID).orElse(null);
            if (sender == null) {
                return new SimpleStringProperty("Unknown User");
            }
            return new SimpleStringProperty(sender.getFirstName() + " " + sender.getLastName());
        });
        statusColumnFriendRequest.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumnFriendRequest.setCellValueFactory(new PropertyValueFactory<>("date"));
        FriendRequestTable.setItems(friendRequestsList);

        // Message Table
        messageTableColumn.setCellValueFactory(cellData -> {
            Utilizator user = cellData.getValue();
            return new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
        });
        messageTableView.setItems(friendsList);
    }

    private void addListeners() {
        // Search User Listener
        SearchUserAll.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Utilizator> filteredList = allUsersList.stream()
                    .filter(u -> u.getFirstName().toLowerCase().contains(newValue.toLowerCase()) ||
                            u.getLastName().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
            usersTableAll.setItems(FXCollections.observableArrayList(filteredList));
        });

        // Send Friend Request Listener
        SendFriendRequest.setOnAction(e -> {
            Utilizator selectedUser = usersTableAll.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                friendshipService.addFriendRequest(loggedInUser.getId(), selectedUser.getId());
                allUsersList.remove(selectedUser);
            } else {
                System.out.println("No user selected.");
            }
        });

        // Accept Friend Request Listener
        AcceptFriendRequest.setOnAction(e -> {
            Prietenie selectedFriendRequest = FriendRequestTable.getSelectionModel().getSelectedItem();
            if (selectedFriendRequest != null) {
                friendshipService.acceptFriendRequest(loggedInUser.getId(), selectedFriendRequest.getId().getE1());
                friendRequestsList.remove(selectedFriendRequest);
                loadFriends();
            }
        });

        // Delete Friend Listener
        DeleteFriend.setOnAction(e -> {
            Utilizator selectedFriend = usersTableFriends.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                friendshipService.removeFriend(loggedInUser.getId(), selectedFriend.getId());
                friendsList.remove(selectedFriend);
                loadAllUsers();
            }
        });

        // Message Table Listener
        messageTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Utilizator friend = messageTableView.getSelectionModel().getSelectedItem();
                List<Message> messages = messageService.getMessages(loggedInUser.getId(), friend.getId());
                messageTextArea.clear();
                for (Message message : messages) {
                    String sender = message.getFrom().getId().equals(loggedInUser.getId()) ? loggedInUser.getFirstName() + " " + loggedInUser.getLastName() : friend.getFirstName() + " " + friend.getLastName();
                    messageTextArea.appendText(sender + ": " + message.getMessage() + "\n");
                }
            }
        });

        // Send Message Listener
        SendMessageButton.setOnAction(e -> {
            List<Utilizator> friends = messageTableView.getSelectionModel().getSelectedItems();
            if (!friends.isEmpty()) {
                String message = MessageTextField.getText();
                if (!message.isBlank()) {
                    messageService.sendMessage(new Message(loggedInUser, friends, message));
                    MessageTextField.clear();
                    messageTextArea.appendText(loggedInUser.getFirstName() + ": " + message + "\n");
                }
            }
        });

        // Show Notification Listener
        ShowNotificationButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setTitle("Friend Requests");
            stage.setWidth(400);
            stage.setHeight(400);

            TableView<Prietenie> friendRequestTable = new TableView<>();
            TableColumn<Prietenie, String> fullNameColumn = new TableColumn<>("From");
            TableColumn<Prietenie, String> statusColumn = new TableColumn<>("Status");
            TableColumn<Prietenie, String> dateColumn = new TableColumn<>("Date");

            fullNameColumn.setCellValueFactory(cellData -> {
                Long senderID = cellData.getValue().getId().getE1();
                Utilizator sender = userService.findUserById(senderID).orElse(null);
                if (sender == null) {
                    return new SimpleStringProperty("Unknown User");
                }
                return new SimpleStringProperty(sender.getFirstName() + " " + sender.getLastName());
            });
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            friendRequestTable.getColumns().addAll(fullNameColumn, statusColumn, dateColumn);
            friendRequestTable.setItems(FXCollections.observableArrayList(friendshipService.findFriendRequests(loggedInUser.getId())));

            VBox vbox = new VBox(friendRequestTable);
            Scene scene = new Scene(vbox);
            stage.setScene(scene);
            stage.show();
        });
    }


    @FXML
    private void handleLogOut() {
        SceneManager.switchScene("/org/example/lab8/login.fxml");
    }

    @FXML
    private void handleDeleteAccount() {
        Utilizator utilizator = userService.findUtilizatorByUsername(LoginController.logedInUsername);
        userService.deleteUtilizator(utilizator.getId());
        SceneManager.switchScene("/org/example/lab8/login.fxml");
    }

    @Override
    public void update(Event event) {
        loadAllUsers();
        loadFriends();
        loadFriendRequests();
    }
}
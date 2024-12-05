package org.example.lab8.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.lab8.domain.Message;
import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.Utilizator;
import org.example.lab8.services.FriendshipService;
import org.example.lab8.services.MessageService;
import org.example.lab8.services.Service;
import org.example.lab8.services.UserService;
import org.example.lab8.utils.events.Event;
import org.example.lab8.utils.observer.Observer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainController implements Observer {
    private final Service mainService = ApplicationContext.getService();
    private final UserService userService = mainService.getUserService();
    private final FriendshipService friendshipService = mainService.getFriendshipService();
    private final MessageService messageService = mainService.getMessageService();
    private final Utilizator loggedInUser = LoginController.logedInUser;


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
    private TableView<Message> message_messageTableView;
    @FXML
    private TableColumn<Message, String> message_messageTableColumn;
    @FXML
    private Button ReplyButton;
    @FXML
    private Button SendToMore_Button;

    // Notificari
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
        messageTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        messageTableView.setItems(friendsList);

        message_messageTableColumn.setCellValueFactory(cellData -> {
            Message message = cellData.getValue();
            Utilizator sender = message.getFrom();
            Utilizator recipient = messageTableView.getSelectionModel().getSelectedItem();

            if (sender != null && recipient != null) {
                String senderName = sender.getId().equals(loggedInUser.getId()) ?
                        loggedInUser.getFirstName() + " " + loggedInUser.getLastName() :
                        recipient.getFirstName() + " " + recipient.getLastName();
                return new SimpleStringProperty(senderName + ": " + message.getMessage());
            }
            return new SimpleStringProperty("");
        });
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
                List<Message> messages = messageService.getMessages(loggedInUser.getId(), newSelection.getId());
                if (messages != null) {
                    message_messageTableView.setItems(FXCollections.observableArrayList(messages));
                } else {
                    message_messageTableView.setItems(FXCollections.observableArrayList());
                }
            }
        });


        //SendMessage listener:
        SendMessageButton.setOnAction(e -> {
            Utilizator selectedFriend = messageTableView.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                String messageText = MessageTextField.getText();
                if (!messageText.isBlank()) {
                    Message newMessage = new Message(loggedInUser, List.of(selectedFriend), messageText);
                    messageService.sendMessage(newMessage);
                    MessageTextField.clear();

                    // Instead of reloading all messages, just add the new one to the existing list
                    ObservableList<Message> currentMessages = message_messageTableView.getItems();
                    if (currentMessages == null) {
                        currentMessages = FXCollections.observableArrayList();
                    }
                    currentMessages.add(newMessage);
                    message_messageTableView.setItems(currentMessages);
                    message_messageTableView.scrollTo(currentMessages.size() - 1); // Auto-scroll to the latest message
                }
            }
        });

        // Reply Button Listener
        ReplyButton.setOnAction(e -> {
            Message selectedMessage = message_messageTableView.getSelectionModel().getSelectedItem();
            Utilizator selectedFriend = messageTableView.getSelectionModel().getSelectedItem();
            if (selectedMessage != null && selectedFriend != null) {
                String replyText = MessageTextField.getText();
                if (!replyText.isBlank()) {
                    String formattedReplyText = "(Replied to: " + selectedMessage.getMessage() + ") " + replyText;
                    Message replyMessage = new Message(loggedInUser, List.of(selectedFriend), formattedReplyText);
                    replyMessage.setReply(selectedMessage);
                    messageService.sendMessage(replyMessage);
                    MessageTextField.clear();

                    // Update the message view
                    ObservableList<Message> currentMessages = message_messageTableView.getItems();
                    if (currentMessages == null) {
                        currentMessages = FXCollections.observableArrayList();
                    }
                    currentMessages.add(replyMessage);
                    message_messageTableView.setItems(currentMessages);
                    message_messageTableView.scrollTo(currentMessages.size() - 1);
                }
            }
        });

        // Multiple Recipients Send Button Listener
        SendToMore_Button.setOnAction(e -> {
            List<Utilizator> selectedFriends = messageTableView.getSelectionModel().getSelectedItems();
            if (!selectedFriends.isEmpty()) {
                String messageText = MessageTextField.getText();
                if (!messageText.isBlank()) {
                    Message newMessage = new Message(loggedInUser, selectedFriends, messageText);
                    messageService.sendMessage(newMessage);
                    MessageTextField.clear();

                    // If the current view shows one of the recipients, update it
                    Utilizator currentlyViewedFriend = messageTableView.getSelectionModel().getSelectedItem();
                    if (currentlyViewedFriend != null && selectedFriends.contains(currentlyViewedFriend)) {
                        ObservableList<Message> currentMessages = message_messageTableView.getItems();
                        if (currentMessages == null) {
                            currentMessages = FXCollections.observableArrayList();
                        }
                        currentMessages.add(newMessage);
                        message_messageTableView.setItems(currentMessages);
                        message_messageTableView.scrollTo(currentMessages.size() - 1);
                    }
                }
            }
        });

        ShowNotificationButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setTitle("Friend Requests");
            stage.setResizable(false);

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
            scene.getStylesheets().add(getClass().getResource("/org/example/lab8/styles/notifications.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        });

    }

    @Override
    public void update(Event event) {
        loadAllUsers();
        loadFriends();
        loadFriendRequests();
    }
}
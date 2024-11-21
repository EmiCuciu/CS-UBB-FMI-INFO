package org.example.lab6.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.example.lab6.domain.Friendship;
import org.example.lab6.domain.FriendshipRequest;
import org.example.lab6.domain.User;
import org.example.lab6.domain.UserInstance;
import org.example.lab6.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

public class FriendRequestsController extends ControllerSuperclass {
    @FXML
    private ListView<FriendshipRequest> listViewRequests;


    private ObservableList<FriendshipRequest> requestList;
    public void init() {
        listViewRequests.setCellFactory(param -> new ListCell<FriendshipRequest>() {
            @Override
            protected void updateItem(FriendshipRequest item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item.toString());
                    setWrapText(true);
                    setPrefWidth(param.getWidth() - 20);
                    setMinHeight(USE_COMPUTED_SIZE);
                    setMaxHeight(USE_PREF_SIZE);
                }
            }
        });
        requestList = FXCollections.observableArrayList();
        listViewRequests.setItems(requestList);
        requestList.clear();
        Optional<User> currentUser = getUserService().getById(UserInstance.getInstance().getId());
        currentUser.ifPresent(user -> getFriendshipRequestService().getRequestsForUser(user)
                .forEach(r -> requestList.add(r)));

    }
    @FXML
    protected void handleButtonAcceptRequestClicked(ActionEvent actionEvent) {
        var selectedItems = listViewRequests.getSelectionModel().getSelectedItems();
        FriendshipRequest friendshipRequest = (FriendshipRequest) selectedItems.getFirst();
        String user1 = UserService.getInstance().getById(friendshipRequest.getSender()).get().getUsername();
        String user2 = UserService.getInstance().getById(friendshipRequest.getReceiver()).get().getUsername();
        Friendship fr = new Friendship(LocalDateTime.now(),user1,user2);
        fr.setId(friendshipRequest.getId());
        getFriendshipService().create(fr);
        requestList.remove(friendshipRequest);
        getFriendshipRequestService().removeRequest(friendshipRequest.getSender(),friendshipRequest.getReceiver());

    }
    @FXML
    protected void handleButtonDeleteRequestClicked(ActionEvent actionEvent) {
        var selectedItems = listViewRequests.getSelectionModel().getSelectedItems();
        FriendshipRequest friendshipRequest = (FriendshipRequest) selectedItems.getFirst();
        requestList.remove(friendshipRequest);
        getFriendshipRequestService().removeRequest(friendshipRequest.getSender(),friendshipRequest.getReceiver());
    }
    @FXML
    protected void backToMain(ActionEvent actionEvent) {
        getScreenService().switchScene("main");
    }
}

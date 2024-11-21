package org.example.lab6.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.lab6.Utils.events.FriendshipEntityChangeEvent;
import org.example.lab6.Utils.observer.Observer;
import org.example.lab6.domain.Friendship;
import org.example.lab6.domain.User;
import org.example.lab6.domain.UserInstance;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainWindowController extends ControllerSuperclass implements Observer<FriendshipEntityChangeEvent> {
    private final ObservableList<Friendship> model = FXCollections.observableArrayList();

    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Friendship> tableView;
    @FXML
    private TableColumn<Friendship, String> tableColumnName;

    public void init() {
        getFriendshipService().addObserver(this);
        initModel();
        tableColumnName.setCellValueFactory(cellData -> {
            Friendship f = cellData.getValue();
            if(f.getId().getE1().equals(UserInstance.getInstance().getId())){
                return new SimpleStringProperty(f.getUser2());
            }else{
                return new SimpleStringProperty(f.getUser1());
            }
        });
        tableView.setItems(model);
    }

    private void initModel() {
        List<Friendship> friends = getFriendshipService().getUserFriends(UserInstance.getInstance().getId());
        model.setAll(friends);
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }

    @FXML
    protected void logout() throws IOException {
        UserInstance.getInstance().setId((long)-1);
        getScreenService().switchScene("login");
    }
    @FXML
    protected void removeFriend() {
        Friendship friendship=(Friendship) tableView.getSelectionModel().getSelectedItem();
        getFriendshipService().delete(friendship.getId());

    }
    @FXML
    protected void addFriend() throws IOException {
        String user = searchBar.getText();
        searchBar.clear();
        Optional<User> snd = getUserService().getById(UserInstance.getInstance().getId());
        Optional<User> rcv = getUserService().getByUsername(user);
        if(rcv.isPresent() && snd.isPresent()){
            getFriendshipRequestService().addRequest(snd.get(),rcv.get());
        }
    }
    @FXML
    protected void toRequests() throws IOException {
        getScreenService().switchScene("friendshipRequests");
    }
}

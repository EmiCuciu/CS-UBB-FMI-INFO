package org.example.lab6.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.lab6.Domain.User;
import org.example.lab6.service.UserService;
import org.example.lab6.utils.events.UserEntityChangeEvent;
import org.example.lab6.utils.observer.Observer;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserEntityChangeEvent> {
    UserService service;
    ObservableList<User> model = FXCollections.observableArrayList();


    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;


    public void setUtilizatorService(UserService service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> messages = service.getAll();
        List<User> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    @Override
    public void update(UserEntityChangeEvent utilizatorEntityChangeEvent) {
        initModel();
    }

    public void handleAddUtilizator(ActionEvent actionEvent) {
        showMessageTaskEditDialog(null);
    }

    public void handleDeleteUtilizator(ActionEvent actionEvent) {
        User user = (User) tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            User deleted = service.deleteUtilizator(user.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Delete user", "Userul a fost sters");
        } else MessageAlert.showErrorMessage(null, "NU ati selectat nici un utilizator");
    }

    public void handleUpdateUtilizator(ActionEvent actionEvent) {
        // TODO
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showMessageTaskEditDialog(selected);
        } else
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un student");

    }

    public void showMessageTaskEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/edit-user-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(service, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.client.gui;

import com.model.Player;
import com.services.IServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController {

    @FXML
    public Button LoginButton;
    @FXML
    public TextField AliasTextField;

    private MainController mainController;
    private Player loggedPlayer;
    private IServices server;
    private Stage stage;
    private Parent mainParent;

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setParent(Parent parent) {
        mainParent = parent;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String alias = AliasTextField.getText().trim();
        loggedPlayer = new Player(alias);

        try{
            server.login(loggedPlayer, mainController);
            System.out.println("User succesfully logged in " + alias);
            Stage stage = new Stage();
            stage.setTitle("Window for " + loggedPlayer.getAlias());
            stage.setScene(new Scene(mainParent));

            stage.show();
            mainController.setLoggedPlayer(loggedPlayer);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("Error logging in " + e + "in GUI");
        }
    }
}

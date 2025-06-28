package gui;

import domain.Jucator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;

import java.io.IOException;

public class LoginController {

    private IServices server;
    private MainController mainController;
    private Jucator jucator;
    private Parent mainParent;
    private static Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField txtAlias;

    @FXML
    private Button btnLogin;


    public void setServer(IServices server) {
        this.server = server;
    }
    public void setJucatorPageController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setParent(Parent mroot) {
        this.mainParent = mroot;
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String alias = txtAlias.getText();

        if (alias == null || alias.trim().isEmpty() || alias.contains(" ")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText(null);
                alert.setContentText("Numele nu poate fi vid sau să conțină spații!");
                alert.showAndWait();
            });
            txtAlias.clear();
            txtAlias.requestFocus();
            return;
        }

        Platform.runLater(() -> {
            try {
                // Încarcă fxml pentru main
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-interface.fxml"));
                Parent root = loader.load();

                // opțional setezi controllerul dacă ai nevoie
                MainController mainCtrl = loader.getController();
                mainCtrl.setServer(server);

                // creează nou stage
                Stage stage = new Stage();
                stage.setTitle("MainPage");
                stage.setScene(new Scene(root));
                stage.show();

                // închide fereastra de login
                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                loginStage.close();

                // alert opțional
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText(null);
                alert.setContentText("Ai făcut login cu numele: " + alias);
                alert.showAndWait();

            } catch (IOException e) {
                logger.error("Eroare la încărcarea Main.fxml", e);
            }
        });
    }


}

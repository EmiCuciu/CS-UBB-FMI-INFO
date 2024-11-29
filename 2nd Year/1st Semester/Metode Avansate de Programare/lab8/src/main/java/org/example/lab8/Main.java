package org.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lab8.controller.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lab8/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mafia Network");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/org/example/lab8/images/money.png"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/**
 * TODO: 1. Sa pot da reply la un mesaj, adica sa schimb areatext din messages in ceva tabela
 *          , unde sa pot da click pe un mesaj si sa am un buton reply
 *          care sa refere acel mesaj, de exemplu replyed to: "mesajul la care se raspunde"
 *          , si sa pot scrie un mesaj nou care sa fie reply la acel mesaj
        2. Sa imi fac observer pentru fiecare db, userdb,friendshipdb, messagedb, unde persistenta datelor sa fie adevarata
        3. Sa fac un sistem de notificari, cand cineva iti trimite un mesaj, sa iti apara o notificare
            adica sa nu fac manareli cu acel buton, sa fie un sistem de notificari
        4. Sa fac un sistem de notificari pentru cand cineva iti trimite un mesaj, sa iti apara un badge(un sclipitor ceva) pe butonul de messages
        5. Sa fac UN OBSERVER PENTRU TOT, adica sa schimb SceneManager din login in main, vreau ca fereastra login sa ramana mereu activa, de acolo sa ma pot conecta cu oricati useri vreau
            scopul final sa am un login, iar celealte ferestre main sa aiba datele persistente pentru fiecare user logat

 */
package org.example.lab6.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lab6.Domain.User;
import org.example.lab6.Domain.Validations.ValidationException;
import org.example.lab6.service.UserService;

import java.time.LocalDateTime;

public class EditUserController {
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;


    private UserService service;
    Stage dialogStage;
    User utilizator;

    @FXML
    private void initialize() {
    }

    public void setService(UserService service,  Stage stage, User u) {
        this.service = service;
        this.dialogStage=stage;
        this.utilizator =u;
        if (null != u) {
            setFields(u);
        }
        textFieldId.setEditable(false);
    }

    @FXML
    public void handleSave(){
        String id=textFieldId.getText();
        String firstNameText= textFieldFirstName.getText();
        String lastNameText= textFieldLastName.getText();

        User utilizator1=new User(firstNameText,lastNameText);
        if (null == this.utilizator)
            saveMessage(utilizator1);
        else{
            utilizator1.setId(Long.valueOf(id));
            updateMessage(utilizator1);
        }
    }

    private void updateMessage(User m)
    {
        try {
            User r= this.service.updateUser(m);
            if (r==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare user","Userul a fost modificat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }


    private void saveMessage(User m)
    {
        // TODO
        try {
            User r= this.service.addUtilizator(m);
            if (r==null)
                dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Slavare user","Mesajul a fost salvat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();

    }

    private void clearFields() {
        textFieldId.setText("");
        textFieldFirstName.setText("");
        textFieldLastName.setText("");

    }
    private void setFields(User u)
    {
        textFieldId.setText(u.getId().toString());
        textFieldFirstName.setText(u.getFirstName());
        textFieldLastName.setText(u.getLastName());

    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}

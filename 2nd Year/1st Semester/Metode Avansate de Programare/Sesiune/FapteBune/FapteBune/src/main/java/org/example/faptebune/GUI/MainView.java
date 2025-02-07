package org.example.faptebune.GUI;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.faptebune.Domain.Nevoie;
import org.example.faptebune.Domain.Persoana;
import org.example.faptebune.Service.Service;
import org.example.faptebune.Utils.Observer;

import java.time.LocalDate;

public class MainView extends BorderPane implements Observer<Nevoie> {
    private Service service;
    private Persoana loggedPerson;
    private TableView<Nevoie> needsTable = new TableView<>();
    private TableView<Nevoie> myHelpTable = new TableView<>();
    private TabPane tabPane = new TabPane();

    public MainView(Service service, Persoana loggedPerson) {
        this.service = service;
        this.loggedPerson = loggedPerson;
        service.addNeedObserver(this);
        initializeComponents();
        refreshTables();
    }

    private void initializeComponents() {
        // Help Others tab
        Tab helpTab = new Tab("Doresc sa ajut!");
        VBox helpBox = new VBox(10);

        setupNeedsTable();
        setupMyHelpTable();

        helpBox.getChildren().addAll(
                new Label("Needs in your city:"),
                needsTable,
                new Label("My help list:"),
                myHelpTable
        );
        helpTab.setContent(helpBox);

        // Need Help tab
        Tab needHelpTab = new Tab("Doresc sa fiu ajutat!");
        VBox needHelpBox = new VBox(10);

        TextField descriptionField = new TextField();
        DatePicker deadlinePicker = new DatePicker();
        Button addButton = new Button("Add Need");

        addButton.setOnAction(e -> {
            service.addNeed(
                    descriptionField.getText(),
                    deadlinePicker.getValue(),
                    loggedPerson.getId()
            );
            descriptionField.clear();
            deadlinePicker.setValue(null);
        });

        needHelpBox.getChildren().addAll(
                new Label("Description:"),
                descriptionField,
                new Label("Deadline:"),
                deadlinePicker,
                addButton
        );
        needHelpTab.setContent(needHelpBox);

        tabPane.getTabs().addAll(helpTab, needHelpTab);
        setCenter(tabPane);
    }

    private void setupNeedsTable() {
        TableColumn<Nevoie, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        TableColumn<Nevoie, LocalDate> dateCol = new TableColumn<>("Deadline");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<Nevoie, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        needsTable.getColumns().addAll(descCol, dateCol, statusCol);

        needsTable.setRowFactory(tv -> {
            TableRow<Nevoie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Nevoie need = row.getItem();
                    if (need.getStatus().equals("Caut erou!") && need.getOmInNevoie() != loggedPerson.getId()) {
                        service.assignHeroToNeed(need.getId(), loggedPerson.getId());
                        showAlert("Nevoia a fost atribuită cu succes!");
                    } else if (need.getOmInNevoie() == loggedPerson.getId()) {
                        showAlert("Nu poți să te selectezi pe tine însuți ca erou pentru nevoia ta.");
                    }
                }
            });
            return row;
        });
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void setupMyHelpTable() {
        TableColumn<Nevoie, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        TableColumn<Nevoie, LocalDate> dateCol = new TableColumn<>("Deadline");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        myHelpTable.getColumns().addAll(descCol, dateCol);
    }

    @Override
    public void update() {
        refreshTables();
    }

    private void refreshTables() {
        needsTable.getItems().setAll(service.getNeedsForCity(loggedPerson.getOras(), loggedPerson.getId()));
        myHelpTable.getItems().setAll(service.getHeroNeeds(loggedPerson.getId()));
    }
}
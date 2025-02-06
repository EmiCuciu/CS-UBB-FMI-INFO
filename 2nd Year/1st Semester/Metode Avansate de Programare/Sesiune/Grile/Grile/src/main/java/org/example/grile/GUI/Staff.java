package org.example.grile.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.grile.Domain.MenuItem;
import org.example.grile.Domain.Order;
import org.example.grile.Service.Service;
import org.example.grile.Utils.Events.Event;
import org.example.grile.Utils.Obs.Observer;

public class Staff implements Observer {
    private final Service service = ApplicationContext.getService();

    @FXML
    public TableView<Order> staffTableView;
    @FXML
    public TableColumn<Order, String> TableColumn;
    @FXML
    public TableColumn<Order, String> DateColumn;
    @FXML
    public TableColumn<Order, String> ItemColumn;


    @FXML
    public void initialize() {
        TableColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ItemColumn.setCellValueFactory(new PropertyValueFactory<>("items"));

        service.addObserver(this);
        updateTable();
    }

    private void updateTable(){
        staffTableView.getItems().setAll(service.getAllOrders());
    }

    @Override
    public void update(Event event) {
        updateTable();
    }
}

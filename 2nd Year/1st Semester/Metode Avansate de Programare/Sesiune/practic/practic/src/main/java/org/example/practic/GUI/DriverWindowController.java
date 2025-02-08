package org.example.practic.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.practic.Domain.*;
import org.example.practic.Service.TaxiService;
import org.example.practic.Utils.Events.*;
import org.example.practic.Utils.Obs.Observer;
import java.util.Map;
import java.util.Optional;

public class DriverWindowController implements Observer {
    @FXML private Label driverNameLabel;
    @FXML private ListView<Order> activeOrdersListView;

    private Driver driver;
    private TaxiService service;

    public void initialize(Driver driver, TaxiService service) {
        this.driver = driver;
        this.service = service;
        service.addObserver(this);

        driverNameLabel.setText("Driver: " + driver.getName());
        setupListView();
        refreshOrders();
    }

    private void setupListView() {
        activeOrdersListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox(10);
                    Label orderInfo = new Label(order.toString());
                    Button finishButton = new Button("Finish");
                    finishButton.setOnAction(e -> handleFinishOrder(order));
                    container.getChildren().addAll(orderInfo, finishButton);
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void handleRefresh() {
        refreshOrders();
    }

    private void refreshOrders() {
        activeOrdersListView.getItems().clear();
        activeOrdersListView.getItems().addAll(service.getActiveOrdersForDriver(driver.getId()));
    }

    private void handleFinishOrder(Order order) {
        service.finishOrder(order);
        refreshOrders();
    }

    @Override
    public void update(Event event) {
        if (event instanceof CursaChangeEvent) {
            Platform.runLater(() -> {
                if (event.getType() == ChangeEventType.NEW_ORDER_NOTIFICATION) {
                    Map<String, Object> data = (Map<String, Object>) event.getData();
                    Driver notifiedDriver = (Driver) data.get("driver");
                    Order order = (Order) data.get("order");

                    if (notifiedDriver.getId().equals(driver.getId())) {
                        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                                String.format("New order: %s -> %s\nAccept?",
                                        order.getPickupAddress(),
                                        order.getDestinationAddress()),
                                ButtonType.YES, ButtonType.NO)
                                .showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            service.acceptOrder(driver.getId(), order);
                            refreshOrders();
                        }
                    }
                } else if (event.getType() == ChangeEventType.ORDER_FINISHED ||
                        event.getType() == ChangeEventType.ORDER_ACCEPTED) {
                    refreshOrders();
                }
            });
        }
    }
}
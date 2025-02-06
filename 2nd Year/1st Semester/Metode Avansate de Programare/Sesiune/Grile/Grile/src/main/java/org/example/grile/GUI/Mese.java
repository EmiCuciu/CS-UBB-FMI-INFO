package org.example.grile.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.grile.Domain.MenuItem;
import org.example.grile.Service.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mese {
    private final Service service = ApplicationContext.getService();
    private int tableId;
    private final Map<MenuItem, CheckBox> checkBoxMap = new HashMap<>();

    @FXML
    private VBox menuVBox;
    @FXML
    private Button placeOrderButton;

    public void setTableId(int tableId) {
        this.tableId = tableId;
        loadItems();
    }

    private void loadItems() {
        List<MenuItem> items = service.getAllMenuItems().stream()
                .sorted((a, b) -> a.getCategory().compareTo(b.getCategory()))
                .collect(Collectors.toList());

        Map<String, List<MenuItem>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));

        ObservableList<VBox> categoryBoxes = FXCollections.observableArrayList();
        for (Map.Entry<String, List<MenuItem>> entry : itemsByCategory.entrySet()) {
            VBox categoryBox = new VBox();
            Label categoryLabel = new Label(entry.getKey());
            categoryBox.getChildren().add(categoryLabel);

            TableView<MenuItem> tableView = new TableView<>();
            TableColumn<MenuItem, String> itemColumn = new TableColumn<>("Item");
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
            TableColumn<MenuItem, String> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> {
                MenuItem menuItem = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(
                        String.format("%.2f %s", menuItem.getPrice(), menuItem.getCurrency())
                );
            });

            TableColumn<MenuItem, CheckBox> selectColumn = new TableColumn<>("Select");
            selectColumn.setCellValueFactory(cellData -> {
                MenuItem menuItem = cellData.getValue();
                CheckBox checkBox = new CheckBox();
                checkBoxMap.put(menuItem, checkBox);
                return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
            });

            tableView.getColumns().addAll(itemColumn, priceColumn, selectColumn);
            tableView.setItems(FXCollections.observableArrayList(entry.getValue()));
            categoryBox.getChildren().add(tableView);

            categoryBoxes.add(categoryBox);
        }
        menuVBox.getChildren().setAll(categoryBoxes);
    }

    @FXML
    private void placeOrder() {
        List<Integer> selectedItems = checkBoxMap.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(entry -> entry.getKey().getId())
                .collect(Collectors.toList()).reversed();

        service.placeOrder(tableId, selectedItems);
    }
}
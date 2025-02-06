package org.example.grile.Repository;

import org.example.grile.Domain.Order;
import org.example.grile.Domain.OrderStatus;
import org.example.grile.Domain.Validation.OrderValidator;

import java.sql.*;
import java.util.*;

public class OrderRepository {
    private final String url;
    private final String user;
    private final String password;
    private final OrderValidator orderValidator;

    public OrderRepository(String url, String username, String password, OrderValidator orderValidator) {
        this.url = url;
        this.user = username;
        this.password = password;
        this.orderValidator = orderValidator;
    }

    public void saveOrder(Order order) throws IllegalArgumentException {
        if (order.getMenuItem().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }



        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Generate a new unique ID for the order
            int newOrderId = generateNewOrderId(connection);

            // Inserting into Orders table
            String sql = "INSERT INTO orders (id, tableid, date, status) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newOrderId);
            preparedStatement.setInt(2, order.getTable());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getDate()));
            preparedStatement.setString(4, order.getStatus().toString());
            preparedStatement.executeUpdate();

            // Inserting into OrderItems table
            String sql2 = "INSERT INTO OrderItems (order_id, menu_item_id) values (?, ?)";
            PreparedStatement itemStatement = connection.prepareStatement(sql2);
            for (Integer menuItemID : order.getMenuItem()) {
                itemStatement.setInt(1, newOrderId);
                itemStatement.setInt(2, menuItemID);
                itemStatement.addBatch();
            }
            itemStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int generateNewOrderId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM orders";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            } else {
                return 1; // If there are no orders, start with ID 1
            }
        }
    }

    public List<Order> findAllPlaced() {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT o.id, o.tableid, o.date, o.status, oi.menu_item_id " +
                    "FROM Orders o " +
                    "JOIN OrderItems oi ON o.id = oi.order_id " +
                    "WHERE o.status = 'PLACED' " +
                    "ORDER BY o.date ASC";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                Map<Integer, Order> orderMap = new HashMap<>();

                while (resultSet.next()) {
                    int orderID = resultSet.getInt("id");
                    Order order = orderMap.get(orderID);
                    if (order == null) {
                        order = new Order(
                                orderID,
                                resultSet.getInt("tableid"),
                                new ArrayList<>(),
                                resultSet.getTimestamp("date").toLocalDateTime(),
                                OrderStatus.valueOf(resultSet.getString("status"))
                        );
                        orderMap.put(orderID, order);
                    }
                    order.getMenuItem().add(resultSet.getInt("menu_item_id"));
                }
                orders.addAll(orderMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<String> getItemNamesByIds(List<Integer> itemsIDs) {
        List<String> itemNames = new ArrayList<>();
        try {
            String sql = "SELECT item FROM menu WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (Integer itemID : itemsIDs) {
                    preparedStatement.setInt(1, itemID);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        itemNames.add(resultSet.getString("item"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemNames;
    }
}
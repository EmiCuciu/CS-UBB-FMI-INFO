package org.example.practic.Repository;

import org.example.practic.Domain.Driver;
import org.example.practic.Domain.Order;
import org.example.practic.Domain.Status;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IRepository<Integer, Order> {
    private final Connection connection;

    public OrderRepository() {
        this.connection = DataBaseConnection.getConnection();
    }

    @Override
    public Order findOne(Integer id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders WHERE id = ?"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractOrderFromResultSet(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(extractOrderFromResultSet(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order save(Order entity) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    """
                    INSERT INTO orders (id, driver_id, status, start_date, end_date, 
                                      pickup_address, destination_address, client_name) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """
            );
            statement.setInt(1, entity.getId());
            if (entity.getDriverId() != null) {
                statement.setInt(2, entity.getDriverId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            statement.setString(3, entity.getStatus().toString());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getStartDate()));
            if (entity.getEndDate() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(entity.getEndDate()));
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }
            statement.setString(6, entity.getPickupAddress());
            statement.setString(7, entity.getDestinationAddress());
            statement.setString(8, entity.getClientName());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order delete(Integer id) {
        Order order = findOne(id);
        if (order != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM orders WHERE id = ?"
                );
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return order;
    }

    @Override
    public Order update(Order entity) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    """
                    UPDATE orders 
                    SET driver_id = ?, status = ?, start_date = ?, end_date = ?, 
                        pickup_address = ?, destination_address = ?, client_name = ? 
                    WHERE id = ?
                    """
            );
            if (entity.getDriverId() != null) {
                statement.setInt(1, entity.getDriverId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setString(2, entity.getStatus().toString());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            if (entity.getEndDate() != null) {
                statement.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
            } else {
                statement.setNull(4, Types.TIMESTAMP);
            }
            statement.setString(5, entity.getPickupAddress());
            statement.setString(6, entity.getDestinationAddress());
            statement.setString(7, entity.getClientName());
            statement.setInt(8, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findActiveOrdersByDriver(Integer driverId) {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders WHERE driver_id = ? AND status = 'IN_PROGRESS'"
            );
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(extractOrderFromResultSet(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Driver> findAvailableDriversSortedByLastOrder() {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                SELECT d.*, MAX(o.end_date) as last_order_date
                FROM drivers d
                LEFT JOIN orders o ON d.id = o.driver_id
                WHERE d.id NOT IN (
                    SELECT DISTINCT driver_id 
                    FROM orders 
                    WHERE status = 'IN_PROGRESS'
                    AND driver_id IS NOT NULL
                )
                GROUP BY d.id, d.name
                ORDER BY last_order_date DESC NULLS LAST
            """);
            ResultSet resultSet = statement.executeQuery();
            List<Driver> drivers = new ArrayList<>();
            while (resultSet.next()) {
                drivers.add(new Driver(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            return drivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Integer driverId = rs.getInt("driver_id");
        if (rs.wasNull()) {
            driverId = null;
        }

        Timestamp endDateTimestamp = rs.getTimestamp("end_date");

        return new Order(
                rs.getInt("id"),
                driverId,
                Status.valueOf(rs.getString("status")),
                rs.getTimestamp("start_date").toLocalDateTime(),
                endDateTimestamp != null ? endDateTimestamp.toLocalDateTime() : null,
                rs.getString("pickup_address"),
                rs.getString("destination_address"),
                rs.getString("client_name")
        );
    }
}
package org.example.practic.Repository;

import org.example.practic.Domain.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository implements IRepository<Integer, Driver> {
    private final Connection connection;

    public DriverRepository() {
        this.connection = DataBaseConnection.getConnection();
    }

    @Override
    public Driver findOne(Integer id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM drivers WHERE id = ?"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Driver(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM drivers"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Driver driver = new Driver(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                drivers.add(driver);
            }
            return drivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Driver save(Driver entity) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO drivers (id, name) VALUES (?, ?)"
            );
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Driver delete(Integer id) {
        Driver driver = findOne(id);
        if (driver != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM drivers WHERE id = ?"
                );
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return driver;
    }

    @Override
    public Driver update(Driver entity) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE drivers SET name = ? WHERE id = ?"
            );
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
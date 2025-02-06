package org.example.grile.Repository;

import org.example.grile.Domain.Table;
import org.example.grile.Domain.Validation.TableValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableRepository {
    private final String url;
    private final String user;
    private final String password;
    private final TableValidator tableValidator;

    public TableRepository(String url, String username, String password, TableValidator tableValidator) {
        this.url = url;
        this.user = username;
        this.password = password;
        this.tableValidator = tableValidator;
    }

    public List<Table> findAll() {
        List<Table> tables = new ArrayList<>();
        String sql = "select * from tables";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tables.add(new Table(resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }


    public Table findById(int tableID) {
        String sql = "select * from tables where id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, tableID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Table(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Table save(Table table) {
        tableValidator.validate(table);
        String sql = "insert into tables values (?)";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, table.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }
}

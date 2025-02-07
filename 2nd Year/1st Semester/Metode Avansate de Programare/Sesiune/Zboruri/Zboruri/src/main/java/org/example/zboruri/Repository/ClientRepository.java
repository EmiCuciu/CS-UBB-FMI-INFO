package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository implements IRepository<Long, Client> {
    @Override
    public Client findOne(Long id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Client findByUsername(String username) {
        String sql = "SELECT * FROM clients WHERE username = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("name")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    @Override
    public Client save(Client entity) {
        String sql = "INSERT INTO clients (username, name) VALUES (?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getName());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client delete(Long id) {
        Client client = findOne(id);
        if (client != null) {
            String sql = "DELETE FROM clients WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return client;
    }

    @Override
    public Client update(Client entity) {
        String sql = "UPDATE clients SET username = ?, name = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getName());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
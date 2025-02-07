package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository implements IRepository<Long, Ticket> {
    @Override
    public Ticket findOne(Long id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Ticket(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getLong("flight_id"),
                        resultSet.getTimestamp("purchase_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM tickets");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Ticket ticket = new Ticket(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getLong("flight_id"),
                        resultSet.getTimestamp("purchase_time").toLocalDateTime()
                );
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tickets;
    }

    public int getTicketCountForFlight(Long flightId) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE flight_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, flightId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public Ticket save(Ticket entity) {
        String sql = "INSERT INTO tickets (username, flight_id, purchase_time) VALUES (?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setLong(2, entity.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));
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
    public Ticket delete(Long id) {
        Ticket ticket = findOne(id);
        if (ticket != null) {
            String sql = "DELETE FROM tickets WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ticket;
    }

    @Override
    public Ticket update(Ticket entity) {
        String sql = "UPDATE tickets SET username = ?, flight_id = ?, purchase_time = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getUsername());
            statement.setLong(2, entity.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
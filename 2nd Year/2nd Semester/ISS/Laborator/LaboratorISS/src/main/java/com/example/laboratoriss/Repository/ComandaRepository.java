package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.ComandaItem;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ComandaRepository implements IComandaRepository {

    private final JdbcUtils dbUtils;
    private final UserRepository userRepository;
    private final ComandaItemRepository comandaItemRepository;

    private static final Logger logger = LogManager.getLogger();
    private static final DateTimeFormatter DB_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String TABLE_NAME = "Comanda"; // Ensure consistent table name

    public ComandaRepository(Properties props, UserRepository userRepository, ComandaItemRepository comandaItemRepository) {
        logger.info("Initializing ComandaRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.userRepository = userRepository;
        this.comandaItemRepository = comandaItemRepository;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DB_FORMATTER) : null;
    }

    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateStr, DB_FORMATTER);
        } catch (Exception e) {
            logger.error("Error parsing date: {}", dateStr, e);
            return null;
        }
    }

    @Override
    public Comanda findOne(Integer id) {
        logger.traceEntry("finding comanda with id: {}", id);

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id = ?")) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String statusString = resultSet.getString("status");
                    String dateStr = resultSet.getString("data");

                    User user = userRepository.findOne(userId);
                    Status status = Status.valueOf(statusString);
                    LocalDateTime data = parseDateTime(dateStr);

                    List<ComandaItem> items = (List<ComandaItem>) comandaItemRepository.findByComandaId(id);

                    return new Comanda(id, items, status, user, data);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding comanda with id {}: {}", id, e.getMessage());
        }

        return null;
    }

    @Override
    public Iterable<Comanda> findAll() {
        logger.traceEntry("finding all comenzi");
        List<Comanda> comenzi = new ArrayList<>();

        Connection connection = dbUtils.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                String statusString = resultSet.getString("status");
                String dateStr = resultSet.getString("data");

                User user = userRepository.findOne(userId);
                Status status = Status.valueOf(statusString);
                LocalDateTime data = parseDateTime(dateStr);

                List<ComandaItem> items = (List<ComandaItem>) comandaItemRepository.findByComandaId(id);

                comenzi.add(new Comanda(id, items, status, user, data));
            }
        } catch (SQLException e) {
            logger.error("Error finding all comenzi: {}", e.getMessage());
        }

        return comenzi;
    }

    @Override
    public List<Comanda> findByUser(User user) {
        logger.traceEntry("finding comenzi for user: {}", user);
        List<Comanda> comenzi = new ArrayList<>();

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?")) {
            statement.setInt(1, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String statusString = resultSet.getString("status");
                    String dateStr = resultSet.getString("data");

                    Status status = Status.valueOf(statusString);
                    LocalDateTime data = parseDateTime(dateStr);

                    List<ComandaItem> items = (List<ComandaItem>) comandaItemRepository.findByComandaId(id);

                    comenzi.add(new Comanda(id, items, status, user, data));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding comenzi for user {}: {}", user, e.getMessage());
        }

        return comenzi;
    }

    @Override
    public List<Comanda> findByStatus(Status status) {
        logger.traceEntry("finding comenzi with status: {}", status);
        List<Comanda> comenzi = new ArrayList<>();

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE status = ?")) {
            statement.setString(1, status.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int userId = resultSet.getInt("user_id");
                    String dateStr = resultSet.getString("data");

                    User user = userRepository.findOne(userId);
                    LocalDateTime data = parseDateTime(dateStr);

                    List<ComandaItem> items = (List<ComandaItem>) comandaItemRepository.findByComandaId(id);

                    comenzi.add(new Comanda(id, items, status, user, data));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding comenzi with status {}: {}", status, e.getMessage());
        }

        return comenzi;
    }

    @Override
    public void save(Comanda comanda) {
        logger.traceEntry("saving comanda {}", comanda);

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (user_id, status, data) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, comanda.getUser().getId());
            statement.setString(2, comanda.getStatus().toString());
            statement.setString(3, formatDateTime(comanda.getData()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comanda failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int comandaId = generatedKeys.getInt(1);
                    comanda.setId(comandaId);

                    // Save each ComandaItem
                    for (ComandaItem item : comanda.getComandaItems()) {
                        item.setId(null);  // Ensure we're saving as new items
                        comandaItemRepository.saveForComanda(item, comandaId);
                    }
                } else {
                    throw new SQLException("Creating comanda failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving comanda {}: {}", comanda, e.getMessage());
        }

        logger.traceExit("save");
    }

    @Override
    public void update(Comanda comanda) {
        logger.traceEntry("updating comanda {}", comanda);

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET user_id = ?, status = ?, data = ? WHERE id = ?")) {

            statement.setInt(1, comanda.getUser().getId());
            statement.setString(2, comanda.getStatus().toString());
            statement.setString(3, formatDateTime(comanda.getData()));
            statement.setInt(4, comanda.getId());

            statement.executeUpdate();

            // Update existing items
            for (ComandaItem item : comanda.getComandaItems()) {
                comandaItemRepository.update(item);
            }
        } catch (SQLException e) {
            logger.error("Error updating comanda {}: {}", comanda, e.getMessage());
        }

        logger.traceExit("update");
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting comanda with id {}", id);

        Connection connection = dbUtils.getConnection();
        try {
            // First delete associated ComandaItems
            comandaItemRepository.deleteByComandaId(id);

            // Then delete the Comanda
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE id = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error deleting comanda with id {}: {}", id, e.getMessage());
        }

        logger.traceExit("delete");
    }
}
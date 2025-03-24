package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.ComandaItem;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ComandaRepository implements IComandaRepository {

    private final JdbcUtils jdbcUtils;
    private final IUserRepository userRepository;
    private final IMedicamentRepository medicamentRepository;
    private final IComandaItemRepository comandaItemRepository;
    private static final Logger logger = LogManager.getLogger();

    public ComandaRepository(Properties properties, IUserRepository userRepository,
                             IMedicamentRepository medicamentRepository,
                             IComandaItemRepository comandaItemRepository) {
        jdbcUtils = new JdbcUtils(properties);
        this.userRepository = userRepository;
        this.medicamentRepository = medicamentRepository;
        this.comandaItemRepository = comandaItemRepository;
    }

    @Override
    public Comanda findOne(Integer id) {
        logger.traceEntry("finding comanda with id: {}", id);
        String sql = "SELECT * FROM Comanda WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer comandaId = resultSet.getInt("id");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Integer userId = resultSet.getInt("user_id");
                    Timestamp timestamp = resultSet.getTimestamp("data");
                    LocalDateTime data = timestamp.toLocalDateTime();

                    User user = userRepository.findOne(userId);
                    List<ComandaItem> items = new ArrayList<>();

                    // Get ComandaItems for this Comanda
                    Iterable<ComandaItem> comandaItems = comandaItemRepository.findByComandaId(comandaId);
                    for (ComandaItem item : comandaItems) {
                        items.add(item);
                    }

                    return new Comanda(comandaId, items, status, user, data);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Comanda> findAll() {
        logger.traceEntry("finding all comenzi");
        List<Comanda> comenzi = new ArrayList<>();
        String sql = "SELECT * FROM Comanda";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer comandaId = resultSet.getInt("id");
                Status status = Status.valueOf(resultSet.getString("status"));
                Integer userId = resultSet.getInt("user_id");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime data = timestamp.toLocalDateTime();

                User user = userRepository.findOne(userId);
                List<ComandaItem> items = new ArrayList<>();

                // Get ComandaItems for this Comanda
                Iterable<ComandaItem> comandaItems = comandaItemRepository.findByComandaId(comandaId);
                for (ComandaItem item : comandaItems) {
                    items.add(item);
                }

                comenzi.add(new Comanda(comandaId, items, status, user, data));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return comenzi;
    }

    @Override
    public List<Comanda> findByUser(User user) {
        logger.traceEntry("finding comenzi for user: {}", user);
        List<Comanda> comenzi = new ArrayList<>();
        String sql = "SELECT * FROM Comanda WHERE user_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Integer comandaId = resultSet.getInt("id");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Timestamp timestamp = resultSet.getTimestamp("data");
                    LocalDateTime data = timestamp.toLocalDateTime();

                    List<ComandaItem> items = new ArrayList<>();

                    // Get ComandaItems for this Comanda
                    Iterable<ComandaItem> comandaItems = comandaItemRepository.findByComandaId(comandaId);
                    for (ComandaItem item : comandaItems) {
                        items.add(item);
                    }

                    comenzi.add(new Comanda(comandaId, items, status, user, data));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return comenzi;
    }

    @Override
    public List<Comanda> findByStatus(Status status) {
        logger.traceEntry("finding comenzi with status: {}", status);
        List<Comanda> comenzi = new ArrayList<>();
        String sql = "SELECT * FROM Comanda WHERE status = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Integer comandaId = resultSet.getInt("id");
                    Integer userId = resultSet.getInt("user_id");
                    Timestamp timestamp = resultSet.getTimestamp("data");
                    LocalDateTime data = timestamp.toLocalDateTime();

                    User user = userRepository.findOne(userId);
                    List<ComandaItem> items = new ArrayList<>();

                    // Get ComandaItems for this Comanda
                    Iterable<ComandaItem> comandaItems = comandaItemRepository.findByComandaId(comandaId);
                    for (ComandaItem item : comandaItems) {
                        items.add(item);
                    }

                    comenzi.add(new Comanda(comandaId, items, status, user, data));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return comenzi;
    }

    @Override
    public void save(Comanda comanda) {
        logger.traceEntry("saving comanda {}", comanda);
        String sqlComanda = "INSERT INTO Comanda(status, user_id, data) VALUES(?, ?, ?)";
        String sqlItem = "INSERT INTO ComandaItem(comanda_id, medicament_id, cantitate) VALUES(?, ?, ?)";

        Connection connection = null;
        PreparedStatement comandaStatement = null;
        PreparedStatement itemStatement = null;

        try {
            connection = jdbcUtils.getConnection();
            connection.setAutoCommit(false);

            // Insert Comanda
            comandaStatement = connection.prepareStatement(sqlComanda, Statement.RETURN_GENERATED_KEYS);
            comandaStatement.setString(1, comanda.getStatus().toString());
            comandaStatement.setInt(2, comanda.getUser().getId());
            comandaStatement.setTimestamp(3, Timestamp.valueOf(comanda.getData()));

            int affectedRows = comandaStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comanda failed, no rows affected.");
            }

            int comandaId;
            try (ResultSet generatedKeys = comandaStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comandaId = generatedKeys.getInt(1);
                    comanda.setId(comandaId);
                } else {
                    throw new SQLException("Creating comanda failed, no ID obtained.");
                }
            }

            // Insert ComandaItems
            itemStatement = connection.prepareStatement(sqlItem);
            for (ComandaItem item : comanda.getComandaItems()) {
                itemStatement.setInt(1, comandaId);
                itemStatement.setInt(2, item.getMedicament().getId());
                itemStatement.setInt(3, item.getCantitate());
                itemStatement.addBatch();
            }
            itemStatement.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("Error during transaction rollback", ex);
                }
            }
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            closeResources(connection, comandaStatement, itemStatement);
        }
        logger.traceExit();
    }

    @Override
    public void update(Comanda comanda) {
        logger.traceEntry("updating comanda {}", comanda);
        String sql = "UPDATE Comanda SET status = ? WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, comanda.getStatus().toString());
            statement.setInt(2, comanda.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting comanda with id {}", id);
        String sqlDeleteItems = "DELETE FROM ComandaItem WHERE comanda_id = ?";
        String sqlDeleteComanda = "DELETE FROM Comanda WHERE id = ?";

        Connection connection = null;
        PreparedStatement deleteItemsStatement = null;
        PreparedStatement deleteComandaStatement = null;

        try {
            connection = jdbcUtils.getConnection();
            connection.setAutoCommit(false);

            // First delete ComandaItems
            deleteItemsStatement = connection.prepareStatement(sqlDeleteItems);
            deleteItemsStatement.setInt(1, id);
            deleteItemsStatement.executeUpdate();

            // Then delete Comanda
            deleteComandaStatement = connection.prepareStatement(sqlDeleteComanda);
            deleteComandaStatement.setInt(1, id);
            deleteComandaStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("Error during transaction rollback", ex);
                }
            }
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            closeResources(connection, deleteItemsStatement, deleteComandaStatement);
        }
        logger.traceExit();
    }

    private void closeResources(Connection conn, PreparedStatement... statements) {
        if (statements != null) {
            for (PreparedStatement stmt : statements) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        logger.error("Error closing statement", e);
                    }
                }
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
}
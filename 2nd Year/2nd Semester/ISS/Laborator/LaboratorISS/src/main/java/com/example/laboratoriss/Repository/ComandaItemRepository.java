package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.ComandaItem;
import com.example.laboratoriss.Domain.Medicament;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ComandaItemRepository implements IComandaItemRepository {

    private final JdbcUtils jdbcUtils;
    private final IMedicamentRepository medicamentRepository;
    private static final Logger logger = LogManager.getLogger();

    public ComandaItemRepository(Properties properties, IMedicamentRepository medicamentRepository) {
        logger.info("Initializing ComandaItemRepository with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public ComandaItem findOne(Integer id) {
        logger.traceEntry("finding ComandaItem with id: {}", id);
        String sql = "SELECT ci.id, ci.comanda_id, ci.medicament_id, ci.cantitate, m.nume, m.pret, m.descriere " +
                "FROM ComandaItem ci " +
                "JOIN Medicament m ON ci.medicament_id = m.id " +
                "WHERE ci.id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int itemId = resultSet.getInt("id");
                    int medicamentId = resultSet.getInt("medicament_id");
                    int cantitate = resultSet.getInt("cantitate");
                    String nume = resultSet.getString("nume");
                    float pret = resultSet.getFloat("pret");
                    String descriere = resultSet.getString("descriere");

                    Medicament medicament = new Medicament(medicamentId, nume, pret, descriere);
                    return new ComandaItem(itemId, medicament, cantitate);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<ComandaItem> findAll() {
        logger.traceEntry("finding all ComandaItems");
        List<ComandaItem> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.comanda_id, ci.medicament_id, ci.cantitate, m.nume, m.pret, m.descriere " +
                "FROM ComandaItem ci " +
                "JOIN Medicament m ON ci.medicament_id = m.id";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                int medicamentId = resultSet.getInt("medicament_id");
                int cantitate = resultSet.getInt("cantitate");
                String nume = resultSet.getString("nume");
                float pret = resultSet.getFloat("pret");
                String descriere = resultSet.getString("descriere");

                Medicament medicament = new Medicament(medicamentId, nume, pret, descriere);
                items.add(new ComandaItem(itemId, medicament, cantitate));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return items;
    }

    @Override
    public Iterable<ComandaItem> findByComandaId(Integer comandaId) {
        logger.traceEntry("finding ComandaItems for comanda: {}", comandaId);
        List<ComandaItem> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.comanda_id, ci.medicament_id, ci.cantitate, m.nume, m.pret, m.descriere " +
                "FROM ComandaItem ci " +
                "JOIN Medicament m ON ci.medicament_id = m.id " +
                "WHERE ci.comanda_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, comandaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int itemId = resultSet.getInt("id");
                    int medicamentId = resultSet.getInt("medicament_id");
                    int cantitate = resultSet.getInt("cantitate");
                    String nume = resultSet.getString("nume");
                    float pret = resultSet.getFloat("pret");
                    String descriere = resultSet.getString("descriere");

                    Medicament medicament = new Medicament(medicamentId, nume, pret, descriere);
                    items.add(new ComandaItem(itemId, medicament, cantitate));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return items;
    }

    @Override
    public void save(ComandaItem entity) {
        logger.traceEntry("saving ComandaItem {}", entity);
        String sql = "INSERT INTO ComandaItem(comanda_id, medicament_id, cantitate) VALUES(?, ?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // We assume the comanda_id is set elsewhere or handled by the service layer
            // This might need adjustment based on your specific application logic
            statement.setInt(1, -1); // Temporary value, needs to be set correctly
            statement.setInt(2, entity.getMedicament().getId());
            statement.setInt(3, entity.getCantitate());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting ComandaItem with id {}", id);
        String sql = "DELETE FROM ComandaItem WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(ComandaItem entity) {
        logger.traceEntry("updating ComandaItem {}", entity);
        String sql = "UPDATE ComandaItem SET medicament_id=?, cantitate=? WHERE id=?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entity.getMedicament().getId());
            statement.setInt(2, entity.getCantitate());
            statement.setInt(3, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    public void saveForComanda(ComandaItem item, int comandaId) {
        logger.traceEntry("saving ComandaItem {} for comanda {}", item, comandaId);
        String sql = "INSERT INTO ComandaItem(comanda_id, medicament_id, cantitate) VALUES(?, ?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, comandaId);
            statement.setInt(2, item.getMedicament().getId());
            statement.setInt(3, item.getCantitate());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    public void deleteByComandaId(Integer id) {
        logger.traceEntry("deleting ComandaItems for comanda with id {}", id);
        String sql = "DELETE FROM ComandaItem WHERE comanda_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}
package com.example.laboratoriss.Repository;

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

public class MedicamentRepository implements IMedicamentRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public MedicamentRepository(Properties properties) {
        logger.info("Initializing MedicamentRepository with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Medicament findOne(Integer id) {
        logger.traceEntry("finding Medicament with id: {}", id);
        String sql = "SELECT * FROM Medicament WHERE id = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int medicamentId = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    Float pret = resultSet.getFloat("pret");
                    String descriere = resultSet.getString("descriere");
                    return new Medicament(medicamentId, nume, pret, descriere);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Medicament> findAll() {
        logger.traceEntry("finding all Medicaments");
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM Medicament";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                Float pret = resultSet.getFloat("pret");
                String descriere = resultSet.getString("descriere");

                medicaments.add(new Medicament(id, nume, pret, descriere));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return medicaments;
    }

    @Override
    public void save(Medicament entity) {
        logger.traceEntry("saving Medicament {}", entity);
        String sql = "INSERT INTO Medicament(nume, pret, descriere) VALUES(?, ?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getNume());
            statement.setFloat(2, entity.getPret());
            statement.setString(3, entity.getDescriere());

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
        logger.traceEntry("deleting Medicament with id {}", id);
        String sql = "DELETE FROM Medicament WHERE id = ?";

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
    public void update(Medicament entity) {
        logger.traceEntry("updating Medicament {}", entity);
        String sql = "UPDATE Medicament SET nume=?, pret=?, descriere=? WHERE id=?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getNume());
            statement.setFloat(2, entity.getPret());
            statement.setString(3, entity.getDescriere());
            statement.setInt(4, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}
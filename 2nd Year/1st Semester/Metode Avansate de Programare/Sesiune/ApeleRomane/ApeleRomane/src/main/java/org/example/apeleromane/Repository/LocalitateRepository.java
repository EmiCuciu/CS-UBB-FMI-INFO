package org.example.apeleromane.Repository;

import org.example.apeleromane.Domain.Localitate;
import org.example.apeleromane.Domain.Rau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalitateRepository implements IRepository<Integer, Localitate> {
    private final RauRepository rauRepository;

    public LocalitateRepository(RauRepository rauRepository) {
        this.rauRepository = rauRepository;
    }

    @Override
    public Localitate findOne(Integer id) {
        String sql = "SELECT * FROM localitati WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Rau rau = rauRepository.findOne(resultSet.getInt("rau_id"));
                return new Localitate(
                        resultSet.getInt("id"),
                        resultSet.getString("nume"),
                        rau,
                        resultSet.getDouble("cota_minima_de_risc"),
                        resultSet.getDouble("cota_maxima_de_risc")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding localitate with id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Localitate> findAll() {
        List<Localitate> localitati = new ArrayList<>();
        String sql = "SELECT * FROM localitati";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Rau rau = rauRepository.findOne(resultSet.getInt("rau_id"));
                Localitate localitate = new Localitate(
                        resultSet.getInt("id"),
                        resultSet.getString("nume"),
                        rau,
                        resultSet.getDouble("cota_minima_de_risc"),
                        resultSet.getDouble("cota_maxima_de_risc")
                );
                localitati.add(localitate);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all localitati", e);
        }
        return localitati;
    }

    @Override
    public Localitate save(Localitate localitate) {
        String sql = "INSERT INTO localitati (nume, rau_id, cota_minima_de_risc, cota_maxima_de_risc) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, localitate.getNume());
            statement.setInt(2, localitate.getRau().getId());
            statement.setDouble(3, localitate.getCotaMinimaDeRisc());
            statement.setDouble(4, localitate.getCotaMaximaAdmisa());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                localitate.setId(rs.getInt(1));
                return localitate;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving localitate", e);
        }
        return null;
    }

    @Override
    public Localitate delete(Integer id) {
        Localitate localitate = findOne(id);
        if (localitate != null) {
            String sql = "DELETE FROM localitati WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                statement.executeUpdate();
                return localitate;
            } catch (SQLException e) {
                throw new RuntimeException("Error deleting localitate with id: " + id, e);
            }
        }
        return null;
    }

    @Override
    public Localitate update(Localitate localitate) {
        String sql = "UPDATE localitati SET nume = ?, rau_id = ?, " +
                "cota_minima_de_risc = ?, cota_maxima_de_risc = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, localitate.getNume());
            statement.setInt(2, localitate.getRau().getId());
            statement.setDouble(3, localitate.getCotaMinimaDeRisc());
            statement.setDouble(4, localitate.getCotaMaximaAdmisa());
            statement.setInt(5, localitate.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return localitate;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating localitate with id: " + localitate.getId(), e);
        }
        return null;
    }

}

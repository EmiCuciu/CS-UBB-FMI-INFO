package org.example.apeleromane.Repository;

import org.example.apeleromane.Domain.Rau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RauRepository implements IRepository<Integer, Rau> {
    @Override
    public Rau findOne(Integer integer) {
        String sql = "SELECT * FROM rauri WHERE id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractRauFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rau> findAll() {
        List<Rau> rauri = new ArrayList<>();
        String sql = "SELECT * FROM rauri";

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                rauri.add((extractRauFromResultSet(resultSet)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rauri;
    }

    @Override
    public Rau save(Rau rau) {
        String sql = "INSERT INTO rauri (nume, cota_medie) VALUES (?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, rau.getNume());
            statement.setDouble(2, rau.getCotaMedie());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                rau.setId(rs.getInt(1));
                return rau;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving rau", e);
        }
        return null;
    }

    @Override
    public Rau delete(Integer id) {
        Rau rau = findOne(id);
        if (rau != null) {
            String sql = "DELETE FROM rauri WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                statement.executeUpdate();
                return rau;
            } catch (SQLException e) {
                throw new RuntimeException("Error deleting rau with id: " + id, e);
            }
        }
        return null;
    }

    @Override
    public Rau update(Rau rau) {
        String sql = "UPDATE rauri SET nume = ?, cota_medie = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rau.getNume());
            statement.setDouble(2, rau.getCotaMedie());
            statement.setInt(3, rau.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return rau;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating rau with id: " + rau.getId(), e);
        }
        return null;
    }

    private Rau extractRauFromResultSet(ResultSet rs) throws SQLException {
        return new Rau(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getDouble("cota_medie")
        );
    }

    public Rau findByName(String numeRau) {
        String sql = "SELECT * FROM rauri WHERE nume = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeRau);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractRauFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



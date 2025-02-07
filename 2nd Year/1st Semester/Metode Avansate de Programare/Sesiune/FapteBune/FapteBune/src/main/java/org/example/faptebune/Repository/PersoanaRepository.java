package org.example.faptebune.Repository;

import org.example.faptebune.Domain.Orase;
import org.example.faptebune.Domain.Persoana;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersoanaRepository implements IRepository<Long, Persoana> {
    @Override
    public Persoana findOne(Long id) {
        String sql = "SELECT * FROM persons WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractPerson(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Persoana> findAll() {
        List<Persoana> persons = new ArrayList<>();
        String sql = "SELECT * FROM persons";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                persons.add(extractPerson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public Persoana save(Persoana person) {
        String sql = "INSERT INTO persons (username, password, first_name, last_name, city) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, person.getUsername());
            pstmt.setString(2, person.getParola());
            pstmt.setString(3, person.getPrenume());
            pstmt.setString(4, person.getNume());
            pstmt.setString(5, person.getOras().toString());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                person.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Persoana delete(Long aLong) {
        String sql = "DELETE FROM persons WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, aLong);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Persoana update(Persoana person) {
        String sql = "UPDATE persons SET username=?, password=?, first_name=?, last_name=?, city=? WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, person.getUsername());
            pstmt.setString(2, person.getParola());
            pstmt.setString(3, person.getPrenume());
            pstmt.setString(4, person.getNume());
            pstmt.setString(5, person.getOras().toString());
            pstmt.setLong(6, person.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Persoana extractPerson(ResultSet rs) throws SQLException {
        Persoana person = new Persoana();
        person.setId(rs.getLong("id"));
        person.setUsername(rs.getString("username"));
        person.setParola(rs.getString("password"));
        person.setPrenume(rs.getString("first_name"));
        person.setNume(rs.getString("last_name"));
        person.setOras(Orase.valueOf(rs.getString("city")));
        return person;
    }
}

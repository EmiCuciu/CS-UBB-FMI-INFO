package org.example.faptebune.Repository;

import org.example.faptebune.Domain.Nevoie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NevoieRepository implements IRepository<Long , Nevoie> {
    @Override
    public Nevoie findOne(Long aLong) {
        String sql = "SELECT * FROM needs WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, aLong);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractNeed(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Nevoie> findAll() {
        List<Nevoie> needs = new ArrayList<>();
        String sql = "SELECT * FROM needs";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                needs.add(extractNeed(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return needs;
    }

    @Override
    public Nevoie save(Nevoie need) {
        String sql = "INSERT INTO needs (description, deadline, person_in_need_id, hero_person_id, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, need.getDescriere());
            pstmt.setDate(2, Date.valueOf(need.getDeadline()));
            pstmt.setLong(3, need.getOmInNevoie());
            pstmt.setObject(4, need.getOmSalvator());
            pstmt.setString(5, need.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                need.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Nevoie delete(Long aLong) {
        String sql = "DELETE FROM needs WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, aLong);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Nevoie update(Nevoie need) {
        String sql = "UPDATE needs SET description=?, deadline=?, person_in_need_id=?, hero_person_id=?, status=? WHERE id=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, need.getDescriere());
            pstmt.setDate(2, Date.valueOf(need.getDeadline()));
            pstmt.setLong(3, need.getOmInNevoie());
            pstmt.setObject(4, need.getOmSalvator());
            pstmt.setString(5, need.getStatus());
            pstmt.setLong(6, need.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Nevoie extractNeed(ResultSet rs) throws SQLException {
        Nevoie need = new Nevoie();
        need.setId(rs.getLong("id"));
        need.setDescriere(rs.getString("description"));
        need.setDeadline(String.valueOf(rs.getDate("deadline").toLocalDate()));
        need.setOmInNevoie(rs.getLong("person_in_need_id"));
        Long heroId = rs.getLong("hero_person_id");
        if (!rs.wasNull()) {
            need.setOmSalvator(heroId);
        }
        need.setStatus(rs.getString("status"));
        return need;
    }
}

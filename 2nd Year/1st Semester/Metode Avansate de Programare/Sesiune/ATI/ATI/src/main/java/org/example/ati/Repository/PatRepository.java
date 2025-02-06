package org.example.ati.Repository;

import org.example.ati.Domain.Pat;
import org.example.ati.Domain.TipPat;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PatRepository implements Repository<Integer, Pat> {
    @Override
    public Pat findOne(Integer id) {
        String sql = "SELECT * FROM paturi WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPatFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pat> findAll() {
        List<Pat> paturi = new ArrayList<>();
        String sql = "SELECT * FROM paturi";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                paturi.add(extractPatFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paturi;
    }

    public List<Pat> getPaturiByTip(TipPat tip) {
        List<Pat> paturi = new ArrayList<>();
        String sql = "SELECT * FROM paturi WHERE tip = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tip.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                paturi.add(extractPatFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paturi;
    }

    public Map<TipPat, Integer> getStatusPaturiLibere() {
        Map<TipPat, Integer> status = new EnumMap<>(TipPat.class);
        String sql = "SELECT tip, COUNT(*) as numar FROM paturi WHERE pacient_cnp IS NULL GROUP BY tip";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TipPat tip = TipPat.valueOf(rs.getString("tip"));
                int numar = rs.getInt("numar");
                status.put(tip, numar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public Pat save(Pat pat) {
        String sql = "INSERT INTO paturi (tip, ventilatie) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pat.getTip().name());
            ps.setBoolean(2, pat.hasVentilatie());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pat.setId(rs.getInt(1));
                return pat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pat update(Pat pat) {
        String sql = "UPDATE paturi SET tip = ?, ventilatie = ?, pacient_cnp = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pat.getTip().name());
            ps.setBoolean(2, pat.hasVentilatie());
            ps.setString(3, pat.getPacientCnp());
            ps.setInt(4, pat.getId());

            if (ps.executeUpdate() > 0) {
                return pat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pat delete(Integer id) {
        Pat pat = findOne(id);
        if (pat != null) {
            String sql = "DELETE FROM paturi WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    return pat;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Pat extractPatFromResultSet(ResultSet rs) throws SQLException {
        Pat pat = new Pat(
                rs.getInt("id"),
                TipPat.valueOf(rs.getString("tip")),
                rs.getBoolean("ventilatie")
        );
        pat.setPacientCnp(rs.getString("pacient_cnp"));
        return pat;
    }
}
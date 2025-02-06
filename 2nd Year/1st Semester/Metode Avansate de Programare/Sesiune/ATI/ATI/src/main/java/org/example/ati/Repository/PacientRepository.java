package org.example.ati.Repository;

import org.example.ati.Domain.Pacient;
import org.example.ati.Domain.StatusPacient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacientRepository implements Repository<String, Pacient> {
    @Override
    public Pacient findOne(String cnp) {
        String sql = "SELECT * FROM pacienti WHERE cnp = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPacientFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pacient> findAll() {
        List<Pacient> pacienti = new ArrayList<>();
        String sql = "SELECT * FROM pacienti";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pacienti.add(extractPacientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacienti;
    }

    public List<Pacient> getPacientiInAsteptare() {
        List<Pacient> pacienti = new ArrayList<>();
        String sql = "SELECT * FROM pacienti WHERE status = 'IN_ASTEPTARE' ORDER BY gravitate DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pacienti.add(extractPacientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacienti;
    }

    @Override
    public Pacient save(Pacient pacient) {
        String sql = "INSERT INTO pacienti (cnp, varste_luni, prematur, diagnostic_principal, gravitate, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pacient.getId());
            ps.setInt(2, pacient.getVarsteLuni());
            ps.setBoolean(3, pacient.isPrematur());
            ps.setString(4, pacient.getDiagnosticPrincipal());
            ps.setInt(5, pacient.getGravitate());
            ps.setString(6, pacient.getStatus().name());

            if (ps.executeUpdate() > 0) {
                return pacient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pacient update(Pacient pacient) {
        String sql = "UPDATE pacienti SET varste_luni = ?, prematur = ?, diagnostic_principal = ?, " +
                "gravitate = ?, status = ? WHERE cnp = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacient.getVarsteLuni());
            ps.setBoolean(2, pacient.isPrematur());
            ps.setString(3, pacient.getDiagnosticPrincipal());
            ps.setInt(4, pacient.getGravitate());
            ps.setString(5, pacient.getStatus().name());
            ps.setString(6, pacient.getId());

            if (ps.executeUpdate() > 0) {
                return pacient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pacient delete(String cnp) {
        Pacient pacient = findOne(cnp);
        if (pacient != null) {
            String sql = "DELETE FROM pacienti WHERE cnp = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, cnp);
                if (ps.executeUpdate() > 0) {
                    return pacient;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Pacient extractPacientFromResultSet(ResultSet rs) throws SQLException {
        Pacient pacient = new Pacient(
                rs.getString("cnp"),
                rs.getInt("varste_luni"),
                rs.getBoolean("prematur"),
                rs.getString("diagnostic_principal"),
                rs.getInt("gravitate")
        );
        pacient.setStatus(StatusPacient.valueOf(rs.getString("status")));
        return pacient;
    }
}

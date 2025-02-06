package org.example.ati_v2.Repository;

import org.example.ati_v2.Domain.Pat;
import org.example.ati_v2.Domain.TipPat;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PatRepository implements Repository<Integer, Pat> {
    @Override
    public Pat findOne(Integer id) {
        String sql = "Select * from paturi where id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractPatFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pat> findAll() {
        List<Pat> paturi = new ArrayList<>();
        String sql = "select * from paturi";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

            while (resultSet.next()){
                paturi.add(extractPatFromResultSet(resultSet));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return paturi;
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

    @Override
    public Pat update(Pat pat) {
        String sql = "UPDATE paturi SET tip = ?, ventilatie = ?, pacient_cnp = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pat.getTip().name());
            ps.setBoolean(2, pat.hasVentilatie());
            ps.setString(3, pat.getPacientCNP());
            ps.setInt(4, pat.getId());

            if (ps.executeUpdate() > 0) {
                return pat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Pat extractPatFromResultSet(ResultSet resultSet) throws SQLException {
        Pat pat = new Pat(
                resultSet.getInt("id"),
                TipPat.valueOf(resultSet.getString("tip")),
                resultSet.getBoolean("ventilatie")
        );
        pat.setPacientCNP(resultSet.getString("pacient_cnp"));
        return pat;
    }



    public List<Pat> getPaturiByTip(TipPat tipPat){
        List<Pat> paturi = new ArrayList<>();
        String sql = "select * from paturi where tip = ?";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, tipPat.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                paturi.add(extractPatFromResultSet(resultSet));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return paturi;
    }


    public Map<TipPat, Integer> getStatusPaturiLibere(){
        Map<TipPat, Integer> status = new EnumMap<>(TipPat.class);
        String sql = "select tip, COUNT(*) as numar from paturi where paturi.pacient_cnp is null group by tip";

        try(Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)){

            while (resultSet.next()){
                TipPat tipPat = TipPat.valueOf(resultSet.getString("tip"));
                int numar = resultSet.getInt("numar");
                status.put(tipPat, numar);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }
}

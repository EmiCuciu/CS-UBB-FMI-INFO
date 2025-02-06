package org.example.ati_v2.Repository;

import org.example.ati_v2.Domain.Pacient;
import org.example.ati_v2.Domain.StatusPacient;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacientRepository implements Repository<String, Pacient> {
    @Override
    public Pacient findOne(String cnp) {
        String sql = "select * from pacienti where cnp = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return extractPacientFromResultSet(rs);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pacient> findAll() {
        List<Pacient> pacienti = new ArrayList<>();
        String sql = "select * from pacienti";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){
                pacienti.add(extractPacientFromResultSet(resultSet));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return pacienti;
    }

    @Override
    public Pacient save(Pacient pacient) {
        String sql = "insert into pacienti values (?,?,?,?,?,?)";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, pacient.getId());
            preparedStatement.setInt(2, pacient.getVarstaLuni());
            preparedStatement.setBoolean(3, pacient.isPrematur());
            preparedStatement.setString(4, pacient.getDiagnostic());
            preparedStatement.setInt(5, pacient.getGravitate());
            preparedStatement.setString(6, pacient.getStatusPacient().name());

            if(preparedStatement.executeUpdate() > 0){
                return pacient;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pacient delete(String cnp) {
        Pacient pacient = findOne(cnp);
        if(pacient != null){
            String sql = "delete from pacienti where cnp = ?";
            try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                preparedStatement.setString(1,cnp);
                if(preparedStatement.executeUpdate() > 0){
                    return pacient;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Pacient update(Pacient pacient) {
        String sql = "update pacienti set varste_luni = ?, prematur = ?, diagnostic_principal =?," +
                "gravitate = ?, status = ? where cnp = ?";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1,pacient.getVarstaLuni());
            preparedStatement.setBoolean(2,pacient.isPrematur());
            preparedStatement.setString(3,pacient.getDiagnostic());
            preparedStatement.setInt(4,pacient.getGravitate());
            preparedStatement.setString(5,pacient.getStatusPacient().name());
            preparedStatement.setString(6,pacient.getId());

            if(preparedStatement.executeUpdate() > 0){
                return pacient;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Pacient extractPacientFromResultSet(ResultSet rs) throws SQLException{
        Pacient pacient = new Pacient(
                rs.getString("cnp"),
                rs.getInt("varste_luni"),
                rs.getBoolean("prematur"),
                rs.getString("diagnostic_principal"),
                rs.getInt("gravitate")
        );
        pacient.setStatusPacient(StatusPacient.valueOf(rs.getString("status")));
        return pacient;
    }


    public List<Pacient> getPacientInAsteptare(){
        List<Pacient> pacienti = new ArrayList<>();
        String sql = "select * from pacienti where status = 'IN_ASTEPTARE' order by gravitate desc";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

            while (resultSet.next()){
                pacienti.add(extractPacientFromResultSet(resultSet));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return pacienti;
    }
}

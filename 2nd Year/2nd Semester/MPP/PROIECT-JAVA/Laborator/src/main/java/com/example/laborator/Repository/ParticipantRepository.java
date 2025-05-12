package com.example.laborator.Repository;

import com.example.laborator.Domain.Participant;
import com.example.laborator.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantRepository implements IParticipantRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;

    public ParticipantRepository(Properties properties) {
        logger.info("Initializing ParticipantRepository with proprieties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("finding Participant with id {}", integer);
        String sql = "SELECT * FROM Participant WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, integer);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Participant(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        List<Participant> participants = new ArrayList<>();
        logger.traceEntry("finding all Participants");
        String sql = "SELECT * FROM Participant";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                participants.add(new Participant(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public void save(Participant entity) {
        logger.traceEntry("saving Participant {}", entity);
        String sql = "INSERT INTO Participant (first_name, last_name) VALUES (?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getFirst_name());
            preparedStatement.setString(2, entity.getLast_name());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
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
    public void delete(Integer integer) {
        logger.traceEntry("deleting Participant with id {}", integer);
        String sql = "DELETE FROM Participant WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Participant entity) {
        logger.traceEntry("updating Participant {}", entity);
        String sql = "UPDATE Participant SET first_name = ?, last_name = ? WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getFirst_name());
            preparedStatement.setString(2, entity.getLast_name());
            preparedStatement.setInt(3, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}
package triatlon.persistence;

import triatlon.model.Arbitru;
import triatlon.persistence.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArbitruRepository implements IArbitruRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public ArbitruRepository(Properties properties) {
        logger.info("Initializing ArbitruRepository with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Arbitru findOne(Integer integer) {
        logger.traceEntry("finding Arbitru with id: {}", integer);
        String sql = "SELECT * FROM Arbitru WHERE id = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, integer);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Arbitru(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
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
    public Iterable<Arbitru> findAll() {
        List<Arbitru> arbitrii = new ArrayList<>();
        logger.traceEntry("finding all Arbitrii");
        String sql = "SELECT * FROM Arbitru";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Arbitru arbitru = new Arbitru(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                );
                arbitrii.add(arbitru);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
        return arbitrii;
    }

    @Override
    public void save(Arbitru entity) {
        logger.traceEntry("saving Arbitru {}", entity);
        String sql = "insert into Arbitru (username, password, first_name, last_name) values (?, ?, ?, ?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirst_name());
            preparedStatement.setString(4, entity.getLast_name());
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
        logger.traceEntry("deleting Arbitru with id: {}", integer);
        String sql = "DELETE FROM Arbitru WHERE id = ?";
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
    public void update(Arbitru entity) {
        logger.traceEntry("updating Arbitru {}", entity);
        String sql = "UPDATE Arbitru SET username = ?, password = ?, first_name = ?, last_name = ? WHERE id = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirst_name());
            preparedStatement.setString(4, entity.getLast_name());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.executeUpdate();  // Added missing executeUpdate call
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Arbitru findBy(String username, String password) {
        logger.traceEntry("finding arbitru with username : {} and password: {}", username, password);
        String sql = "SELECT * FROM Arbitru WHERE username = ? AND password = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Arbitru(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
        return null;
    }
}
package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Domain.UserType;
import com.example.laboratoriss.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepository implements IUserRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserRepository(Properties properties) {
        logger.info("Initializing UserRepository with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public User findOne(Integer integer) {
        logger.traceEntry("finding Users with id: {}", integer);
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, integer);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String typeString = rs.getString("type");
                    UserType type = UserType.valueOf(typeString);
                    String nume = rs.getString("nume");
                    String prenume = rs.getString("prenume");

                    if (type == UserType.FARMACIST) {
                        return new User(id, username, password, type, nume, prenume);
                    } else {
                        String sectie = rs.getString("sectie");
                        return new User(id, username, password, type, nume, prenume, sectie);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry("finding all Users");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                UserType type = UserType.valueOf(resultSet.getString("type"));
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");

                if (type == UserType.FARMACIST) {
                    users.add(new User(id, username, password, type, nume, prenume));
                } else {
                    String sectie = resultSet.getString("sectie");
                    users.add(new User(id, username, password, type, nume, prenume, sectie));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("saving User {}", entity);
        String sql;

        if (entity.getType() == UserType.FARMACIST) {
            sql = "INSERT INTO Users(username, password, type, nume, prenume) VALUES(?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Users(username, password, type, nume, prenume, sectie) VALUES(?, ?, ?, ?, ?, ?)";
        }

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getType().toString());
            statement.setString(4, entity.getNume());
            statement.setString(5, entity.getPrenume());

            if (entity.getType() == UserType.PERSONAL_SECTIE) {
                statement.setString(6, entity.getSectie());
            }

            int affectedRows = statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting User with id {}", id);
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(User entity) {
        logger.traceEntry("updating User {}", entity);
        String sql;

        if (entity.getType() == UserType.FARMACIST) {
            sql = "UPDATE Users SET username=?, password=?, type=?, nume=?, prenume=? WHERE id=?";
        } else {
            sql = "UPDATE Users SET username=?, password=?, type=?, nume=?, prenume=?, sectie=? WHERE id=?";
        }

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getType().toString());
            statement.setString(4, entity.getNume());
            statement.setString(5, entity.getPrenume());

            if (entity.getType() == UserType.PERSONAL_SECTIE) {
                statement.setString(6, entity.getSectie());
                statement.setInt(7, entity.getId());
            } else {
                statement.setInt(6, entity.getId());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}

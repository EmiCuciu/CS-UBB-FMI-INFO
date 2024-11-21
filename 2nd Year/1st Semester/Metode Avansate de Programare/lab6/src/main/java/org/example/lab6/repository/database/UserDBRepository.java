package org.example.lab6.repository.database;

import org.example.lab6.domain.User;
import org.example.lab6.domain.validation.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBRepository extends AbstractDBRepository<Long, User> {

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Optional<User> findOne(Long id) {
        String FIND_ONE_QUERY = "select * from users where id=?";
        try (Connection connection = prepareConcection();
             PreparedStatement ps = connection.prepareStatement(FIND_ONE_QUERY)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next())
                return Optional.of(createUser(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        String FIND_ALL_QUERY = "select * from users";
        try (Connection connection = prepareConcection();
             PreparedStatement ps = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next())
                users.add(createUser(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        String sql = "INSERT INTO users (id, first_name, last_name, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/social_network", "postgres", "emi12345");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getUsername());
            statement.setString(5, entity.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(Long id) {
        Optional<User> user = findOne(id);
        String DELETE_QUERY = "delete from users where id=?";
        try (Connection connection = prepareConcection();
             PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
            ps.setLong(1, id);
            ps.execute();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }


    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
        user.setId(resultSet.getLong(1));
        return user;
    }
}

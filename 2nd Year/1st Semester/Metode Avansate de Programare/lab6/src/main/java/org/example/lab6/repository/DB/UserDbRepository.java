package org.example.lab6.repository.DB;

import org.example.lab6.Domain.User;
import org.example.lab6.Domain.Validations.Validator;
import org.example.lab6.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * @param id - long, the id of a user to found
     * @return Optional<User> - the user with the given id
     *                        -Optional.empty() otherwise
     */
    @Override
    public Optional<User> findOne(Long id) {
        User user;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(String.format("select * from users U where U.id = '%d'", id))) {
            if(resultSet.next()){
                user = createUserFromResultSet(resultSet);
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private User createUserFromResultSet(ResultSet resultSet) {
        try {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            Long idd = resultSet.getLong("id");
            User user = new User(firstName, lastName);
            user.setId(idd);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User utilizator = new User(firstName, lastName);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        String sql = "insert into users (first_name, last_name) values (?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long id) {
        String sql = "delete from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Optional<User> user = findOne(id);
            if(!user.isEmpty()) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User user) {
        if(user == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(user);
        String sql = "update users set first_name = ?, last_name = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setLong(3, user.getId());
            if( ps.executeUpdate() > 0 )
                return Optional.empty();
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

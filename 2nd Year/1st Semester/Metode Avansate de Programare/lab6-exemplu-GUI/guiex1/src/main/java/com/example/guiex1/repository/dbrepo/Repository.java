package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Tuple;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Repository implements com.example.guiex1.repository.Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private UtilizatorValidator validator;

    public Repository(String url, String username, String password, UtilizatorValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        // Implementation here
        return Optional.empty();
    }

    protected Prietenie createEntityFromResult(ResultSet resultSet) throws SQLException {
        Long userId1 = resultSet.getLong("user_id");
        Long friendId = resultSet.getLong("friend_id");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        String status = resultSet.getString("status");
        Prietenie request = new Prietenie(date, status);
        request.setId(new Tuple<>(userId1, friendId));
        return request;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator user = new Utilizator(firstName, lastName);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        // Implementation here
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        // Implementation here
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        // Implementation here
        return Optional.empty();
    }

    public void addFriendRequest(Long userId, Long friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id, status, date) VALUES (?, ?, 'pending', ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, friendId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        String sql = "UPDATE friendships SET status = 'accepted' WHERE user_id = ? AND friend_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, friendId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add reciprocal entry
        String reciprocalSql = "INSERT INTO friendships (user_id, friend_id, status, date) VALUES (?, ?, 'accepted', ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(reciprocalSql)) {
            ps.setLong(1, userId);
            ps.setLong(2, friendId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friendships WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, friendId);
            ps.setLong(3, friendId);
            ps.setLong(4, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        Set<Prietenie> requests = new HashSet<>();
        String sql = "SELECT * FROM friendships WHERE friend_id = ? AND status = 'pending'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long userId1 = resultSet.getLong("user_id");
                    Long friendId = resultSet.getLong("friend_id");
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Prietenie request = new Prietenie(date, status);
                    request.setId(new Tuple<>(userId1, friendId));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public Set<Utilizator> findFriends(Long userId) {
        Set<Utilizator> friends = new HashSet<>();
        String sql = "SELECT u.* FROM users u INNER JOIN friendships f ON u.id = f.friend_id WHERE f.user_id = ? AND f.status = 'accepted'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Utilizator friend = new Utilizator(firstName, lastName);
                    friend.setId(id);
                    friends.add(friend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public Optional<Utilizator> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Utilizator user = new Utilizator(firstName, lastName);
                    user.setId(id);
                    user.setUsername(username);
                    user.setPassword(password);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void saveUser(Utilizator user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
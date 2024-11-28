package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Tuple;
import com.example.guiex1.domain.Utilizator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PrietenieDBRepository extends AbstractDBRepository<Prietenie> {
    PrietenieValidator validator;

    public PrietenieDBRepository(String url, String username, String password, PrietenieValidator validator) {
        super(url, username, password);
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Long id) {
        Set<Prietenie> requests = new HashSet<>();
        String sql = "SELECT * FROM friendships WHERE friend_id = ? AND status = 'pending'";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
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
        return requests.stream().findFirst();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        // Implementation here
        return null;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        // Implementation here
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> delete(Long id) {
        // Implementation here
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        // Implementation here
        return Optional.empty();
    }

    public void addFriendRequest(Long userId, Long friendId) {
        String checkSql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setLong(1, userId);
            checkPs.setLong(2, friendId);
            try (ResultSet resultSet = checkPs.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    System.out.println("Friend request already exists.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO friendships (user_id, friend_id, status, date) VALUES (?, ?, 'pending', ?)";
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, friendId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add reciprocal entry
        String reciprocalSql = "INSERT INTO friendships (user_id, friend_id, status, date) VALUES (?, ?, 'accepted', ?)";
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
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
}
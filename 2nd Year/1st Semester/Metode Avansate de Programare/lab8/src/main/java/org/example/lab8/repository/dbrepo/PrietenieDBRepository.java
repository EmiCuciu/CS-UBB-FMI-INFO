package org.example.lab8.repository.dbrepo;


import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.validators.PrietenieValidator;
import org.example.lab8.domain.Tuple;
import org.example.lab8.domain.Utilizator;
import org.example.lab8.utils.paging.Page;
import org.example.lab8.utils.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

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
        String sql = "UPDATE    friendships SET status = 'accepted' WHERE user_id = ? AND friend_id = ?";
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
    @Override
    public Page<Prietenie> findAllOnPage(Pageable pageable) {
        try (Connection connection = getConnection()) {
            int totalNumberOfFriendships = count(connection);
            List<Prietenie> friendshipsOnPage = new ArrayList<>();

            if (totalNumberOfFriendships > 0) {
                friendshipsOnPage = findAllOnPageInternal(connection, pageable);
            }

            return new Page<>(friendshipsOnPage, totalNumberOfFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Prietenie> findAllOnPage(Pageable pageable, Long userId) {
        try (Connection connection = getConnection()) {
            int totalNumberOfFriendships = countFriendshipsOfUser(connection, userId);
            List<Prietenie> friendshipsOnPage = new ArrayList<>();

            if (totalNumberOfFriendships > 0) {
                friendshipsOnPage = findAllOnPageForUser(connection, pageable, userId);
            }

            return new Page<>(friendshipsOnPage, totalNumberOfFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int count(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM friendships WHERE status = 'accepted'";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    private int countFriendshipsOfUser(Connection connection, Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM friendships WHERE (user_id = ? OR friend_id = ?) AND status = 'accepted'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    private List<Prietenie> findAllOnPageInternal(Connection connection, Pageable pageable) throws SQLException {
        List<Prietenie> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships WHERE status = 'accepted' " +
                "ORDER BY date " + // Add an ORDER BY clause
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageable.getPageSize() * pageable.getPageNumber());
            statement.setInt(2, pageable.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long userId1 = resultSet.getLong("user_id");
                    Long userId2 = resultSet.getLong("friend_id");
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    String status = resultSet.getString("status");

                    Prietenie prietenie = new Prietenie(date, status);
                    prietenie.setId(new Tuple<>(userId1, userId2));
                    friendships.add(prietenie);
                }
            }
        }

        return friendships;
    }

    private List<Prietenie> findAllOnPageForUser(Connection connection, Pageable pageable, Long userId) throws SQLException {
        List<Prietenie> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships " +
                "WHERE (user_id = ? OR friend_id = ?) AND status = 'accepted' " +
                "ORDER BY date " + // Add an ORDER BY clause
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
            statement.setInt(4, pageable.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long userId1 = resultSet.getLong("user_id");
                    Long userId2 = resultSet.getLong("friend_id");
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    String status = resultSet.getString("status");

                    Prietenie prietenie = new Prietenie(date, status);
                    prietenie.setId(new Tuple<>(userId1, userId2));
                    friendships.add(prietenie);
                }
            }
        }

        return friendships;
    }
}
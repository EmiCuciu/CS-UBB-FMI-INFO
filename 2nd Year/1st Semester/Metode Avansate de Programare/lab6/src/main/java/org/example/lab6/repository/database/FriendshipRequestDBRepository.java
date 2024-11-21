package org.example.lab6.repository.database;

import org.example.lab6.domain.FriendshipRequest;
import org.example.lab6.domain.Tuple;
import org.example.lab6.domain.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestDBRepository extends AbstractDBRepository<Tuple<Long, Long>, FriendshipRequest> {

    public FriendshipRequestDBRepository(String url, String username, String password, Validator<FriendshipRequest> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Optional<FriendshipRequest> findOne(Tuple<Long, Long> requestID) {
        String sql = "SELECT * FROM friendshiprequest WHERE sender = ? AND receiver = ?";
        try (Connection connection = prepareConcection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, requestID.getE1());
            ps.setLong(2, requestID.getE2());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long userId1 = rs.getLong("sender");
                Long userId2 = rs.getLong("receiver");
                LocalDateTime requestDate = rs.getTimestamp("dateSent").toLocalDateTime();
                String status = rs.getString("status");
                return Optional.of(new FriendshipRequest(userId1, userId2, requestDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        List<FriendshipRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM friendshiprequest";
        try (Connection connection = prepareConcection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long userId1 = rs.getLong("sender");
                Long userId2 = rs.getLong("receiver");
                LocalDateTime requestDate = rs.getTimestamp("dateSent").toLocalDateTime();
                String status = rs.getString("status");
                requests.add(new FriendshipRequest(userId1, userId2, requestDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        String sql = "INSERT INTO friendshiprequest (\"sender\", \"receiver\", datesent, \"status\") VALUES (?, ?, ?, ?)";
        try (Connection connection = prepareConcection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getSender());
            ps.setLong(2, entity.getReceiver());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDateSent()));
            ps.setString(4, entity.getStatus());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendshipRequest> delete(Tuple<Long, Long> request) {
        String sql = "DELETE FROM friendshiprequest WHERE sender = ? AND receiver = ?";
        try (Connection connection = prepareConcection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, request.getE1());
            ps.setLong(2, request.getE2());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
                return Optional.of(new FriendshipRequest(request.getE1(), request.getE2(), LocalDateTime.now(), "deleted"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        return Optional.empty();
    }
}

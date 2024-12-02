package org.example.lab8.repository.dbrepo;

import org.example.lab8.domain.Message;
import org.example.lab8.domain.Utilizator;
import org.example.lab8.domain.validators.MessageValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository extends AbstractDBRepository<Message> {
    MessageValidator messageValidator;

    public MessageDBRepository(String url, String username, String password, MessageValidator messageValidator) {
        super(url, username, password);
        this.messageValidator = messageValidator;
    }



    @Override
    public Optional<Message> findOne(Long id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToMessage(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                messages.add(mapResultSetToMessage(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String sql = "INSERT INTO messages (from_user_id, to_user_id, message, data, reply_to_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, entity.getFrom().getId());
            ps.setLong(2, entity.getTo().get(0).getId()); // Assuming single recipient for simplicity
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getData()));
            if (entity.getReply() != null) {
                ps.setLong(5, entity.getReply().getId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long id) {
        Optional<Message> message = findOne(id);
        if (message.isPresent()) {
            String sql = "DELETE FROM messages WHERE id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @Override
    public Optional<Message> update(Message entity) {
        String sql = "UPDATE messages SET from_user_id = ?, to_user_id = ?, message = ?, data = ?, reply_to_id = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getFrom().getId());
            ps.setLong(2, entity.getTo().get(0).getId()); // Assuming single recipient for simplicity
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getData()));
            if (entity.getReply() != null) {
                ps.setLong(5, entity.getReply().getId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    private Message mapResultSetToMessage(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long fromUserId = resultSet.getLong("from_user_id");
        Long toUserId = resultSet.getLong("to_user_id");
        String messageText = resultSet.getString("message");
        LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
        Long replyToId = resultSet.getLong("reply_to_id");

        Utilizator from = new Utilizator(); // Assuming Utilizator class has a default constructor
        from.setId(fromUserId);
        Utilizator to = new Utilizator(); // Assuming Utilizator class has a default constructor
        to.setId(toUserId);

        Message message = new Message(id, from, List.of(to), messageText, data);
        if (replyToId != 0) {
            message.setReply(findOne(replyToId).orElse(null));
        }
        return message;
    }

    public List<Message> getMessages(Long userId1, Long userId2) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId1);
            ps.setLong(2, userId2);
            ps.setLong(3, userId2);
            ps.setLong(4, userId1);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(mapResultSetToMessage(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }



}
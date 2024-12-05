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
        String sql = "INSERT INTO messages (from_user_id, message, data, reply_to_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, entity.getFrom().getId());
            ps.setString(2, entity.getMessage());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getData()));
            if (entity.getReply() != null) {
                ps.setLong(4, entity.getReply().getId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            ps.executeUpdate();

            Long messageId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    messageId = generatedKeys.getLong(1);
                    entity.setId(messageId);
                } else {
                    return Optional.of(entity);
                }
            }

            // Save recipients
            String recipientSql = "INSERT INTO messages_recipients (message_id, recipient_id) VALUES (?, ?)";
            try (PreparedStatement recipientPs = connection.prepareStatement(recipientSql)) {
                for (Utilizator recipient : entity.getTo()) {
                    recipientPs.setLong(1, messageId);
                    recipientPs.setLong(2, recipient.getId());
                    recipientPs.executeUpdate();
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
        String sql = "UPDATE messages SET from_user_id  = ?, message = ?, data = ?, reply_to_id = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getFrom().getId());
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
        String messageText = resultSet.getString("message");
        LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
        Long replyToId = resultSet.getLong("reply_to_id");

        // Get the sender
        Utilizator from = new Utilizator();
        from.setId(fromUserId);

        // Get all recipients for this message
        List<Utilizator> recipients = new ArrayList<>();
        String recipientsSql = "SELECT u.* FROM users u " +
                "JOIN messages_recipients mr ON u.id = mr.recipient_id " +
                "WHERE mr.message_id = ?";

        try (Connection recipientsConnection = getConnection();
             PreparedStatement recipientsPs = recipientsConnection.prepareStatement(recipientsSql)) {
            recipientsPs.setLong(1, id);
            try (ResultSet recipientsRs = recipientsPs.executeQuery()) {
                while (recipientsRs.next()) {
                    Utilizator recipient = new Utilizator();
                    recipient.setId(recipientsRs.getLong("id"));
                    recipient.setFirstName(recipientsRs.getString("first_name"));
                    recipient.setLastName(recipientsRs.getString("last_name"));
                    recipients.add(recipient);
                }
            }
        }

        Message message = new Message(id, from, recipients, messageText, data);

        // Set reply message if exists
        if (replyToId != 0) {
            // Avoid infinite recursion by only setting the ID of the reply message
            Message replyMessage = findOne(replyToId).orElse(null);
            message.setReply(replyMessage);
        }

        return message;
    }

    public List<Message> getMessages(Long userId1, Long userId2) {
        List<Message> messages = new ArrayList<>();
        String sql = """
        SELECT DISTINCT m.* FROM messages m 
        JOIN messages_recipients mr ON m.id = mr.message_id 
        WHERE (m.from_user_id = ? AND mr.recipient_id = ?) 
           OR (m.from_user_id = ? AND mr.recipient_id = ?)
        ORDER BY m.data
    """;
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
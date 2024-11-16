package src.repository.database;

import src.domain.Entity;
import src.domain.Prietenie;
import src.domain.validators.Validator;
import src.repository.database.database_utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;

public class FriendshipDBRepo extends AbstractDatabaseRepo<Long, Prietenie> {

    public FriendshipDBRepo(Validator<Prietenie> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected Prietenie extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long user1 = resultSet.getLong("user1_id");
        Long user2 = resultSet.getLong("user2_id");
        LocalDateTime date = resultSet.getTimestamp("datefriendship").toLocalDateTime();

        Prietenie friendship = new Prietenie(user1, user2, date);
        friendship.setId(id);

        return friendship;
    }

    @Override
    protected String createInsertStatement(Prietenie entity) {
        return "INSERT INTO " + super.tableName + " (user1_id, user2_id, datefriendship) VALUES (?, ?, ?)";
    }

    @Override
    protected String createUpdateStatement(Prietenie entity) {
        return "UPDATE " + super.tableName + " SET user1_id = ?, user2_id = ?, datefriendship = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getIdUser1());
        statement.setLong(2, entity.getIdUser2());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getIdUser1());
        statement.setLong(2, entity.getIdUser2());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        statement.setLong(4, entity.getId());
    }

    @Override
    protected void putIdOnEntity(Entity<Long> entity, Object generatedId) {
        entity.setId(((Number) generatedId).longValue());
    }

    public void removePrietenie(Long id1, Long id2) {
        String sql = "DELETE FROM " + super.tableName + " WHERE user1_id = ? AND user2_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id1);
            stmt.setLong(2, id2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting friendship from database", e);
            throw new RuntimeException("Error deleting friendship from database", e);
        }
    }
}

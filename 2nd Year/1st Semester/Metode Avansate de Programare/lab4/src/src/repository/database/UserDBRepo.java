package src.repository.database;

import src.domain.Entity;
import src.domain.User;
import src.domain.validators.Validator;
import src.repository.database.database_utils.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepo extends AbstractDatabaseRepo<Long, User> {

    public UserDBRepo(Validator<User> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected User extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");

        User user = new User(firstName, lastName);
        user.setId(id);

        return user;
    }

    @Override
    protected String createInsertStatement(User entity) {
        return "INSERT INTO " + super.tableName + "(first_name, last_name) VALUES (?, ?)";
    }

    @Override
    protected String createUpdateStatement(User entity) {
        return "UPDATE " + super.tableName + " SET first_name = ?, last_name = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setLong(3, entity.getId());
    }

    @Override
    protected void putIdOnEntity(Entity<Long> entity, Object generatedId) {
        entity.setId(((Number) generatedId).longValue());
    }

    public void removeUser(Long id) {
        String sql = "DELETE FROM " + super.tableName + " WHERE id = ?";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

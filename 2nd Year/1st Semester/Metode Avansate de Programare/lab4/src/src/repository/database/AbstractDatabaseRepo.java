package src.repository.database;

import src.domain.Entity;
import src.domain.validators.Validator;
import src.repository.database.database_utils.DatabaseUtil;
import src.repository.memory.InMemoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDatabaseRepo<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    protected final String tableName;
    protected static final Logger logger = Logger.getLogger(AbstractDatabaseRepo.class.getName());

    public AbstractDatabaseRepo(Validator<E> validator, String tableName) {
        super(validator);
        this.tableName = tableName;
        loadData();
    }

    private void loadData() {
        String query = "SELECT * FROM " + this.tableName;

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                E entity = extractEntity(resultSet);
                super.save(entity);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading data from database", e);
            throw new RuntimeException("Error loading data from database", e);
        }
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract String createInsertStatement(E entity);

    protected abstract String createUpdateStatement(E entity);

    protected abstract void putIdOnEntity(Entity<ID> entity, Object generatedId);

    public Optional<E> save(E entity) {
        this.validator.validate(entity);
        String insertSql = createInsertStatement(entity) + " RETURNING id";
        Object generatedId;

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            setInsertParameters(statement, entity);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    generatedId = resultSet.getObject("id");
                    putIdOnEntity(entity, generatedId);
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }

            return super.save(entity);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving entity to the database", e);
            throw new RuntimeException("Error saving entity to the database", e);
        }
    }

    protected abstract void setInsertParameters(PreparedStatement statement, E entity) throws SQLException;

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> result = super.delete(id);
        if (result.isPresent()) {
            String deleteQuery = "DELETE FROM " + this.tableName + " WHERE id = ?";

            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

                statement.setObject(1, id);
                statement.executeUpdate();

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error deleting data from database", e);
                throw new RuntimeException("Error deleting data from database", e);
            }
        }
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);

        if (result.isPresent()) {
            String updateQuery = createUpdateStatement(entity);

            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(updateQuery)) {

                setUpdateParameters(statement, entity);
                statement.executeUpdate();

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error updating data in the database", e);
                throw new RuntimeException("Error updating data in the database", e);
            }
        }
        return result;
    }

    protected abstract void setUpdateParameters(PreparedStatement statement, E entity) throws SQLException;
}

package persistence.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID, T> {
    // Save an entity
    void save(T entity);

    // Delete an entity by ID
    void delete(ID id);

    // Find an entity by ID
    Optional<T> findOne(ID id);

    // Returns all entities
    List<T> findAll();

    // Update an entity
    void update(T entity);
}

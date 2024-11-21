package org.example.lab6.service;



import org.example.lab6.domain.Entity;
import org.example.lab6.repository.Repository;

import java.util.Optional;

public interface EntityService<ID, E extends Entity<ID>> {

    // Abstract method to provide the repository
    Repository<ID, E> getRepo();

    // Default methods for CRUD operations
    default Optional<E> create(E entity) {
        return getRepo().save(entity);
    }

    default Optional<E> update(E entity) {
        return getRepo().update(entity);
    }

    default Optional<E> delete(ID entityId) {
        return getRepo().delete(entityId);
    }

    default Iterable<E> getAll() {
        return getRepo().findAll();
    }

    default Optional<E> getById(ID entityId) {
        return getRepo().findOne(entityId);
    }
}

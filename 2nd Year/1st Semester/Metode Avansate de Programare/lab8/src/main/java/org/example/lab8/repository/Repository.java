package org.example.lab8.repository;

import java.util.Optional;

public interface Repository<ID, E> {
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
    Optional<E> delete(ID id);
    Optional<E> update(E entity);
}
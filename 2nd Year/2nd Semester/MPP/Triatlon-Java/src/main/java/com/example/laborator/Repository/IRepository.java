package com.example.laborator.Repository;

import com.example.laborator.Domain.Entity;

public interface IRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);

    Iterable<E> findAll();

    void save(E entity);

    void delete(ID id);

    void update(E entity);

}
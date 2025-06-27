package com.persistence;

import com.model.Entity;

import java.io.Serializable;

public interface IRepository<ID extends Serializable, E extends Entity<ID>> {
    E findOne(ID id);

    Iterable<E> findAll();

    void save(E entity);

    void delete(ID id);

    void update(E entity);
}

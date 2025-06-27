package com.persistence;

import com.model.Entiti;

public interface IRepository<ID, E extends Entiti<ID>> {
    E findOne(ID id);

    Iterable<E> findAll();

    void save(E entity);

    void delete(ID id);

    void update(E entity);
}
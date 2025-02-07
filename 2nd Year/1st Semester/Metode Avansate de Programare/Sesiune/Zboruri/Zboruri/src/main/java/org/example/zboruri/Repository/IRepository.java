package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Entity;

import java.util.List;

public interface IRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);

    List<E> findAll();

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}

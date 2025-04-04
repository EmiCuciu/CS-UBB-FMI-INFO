package org.example.ati.Repository;

import org.example.ati.Domain.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);

    List<E> findAll();

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}

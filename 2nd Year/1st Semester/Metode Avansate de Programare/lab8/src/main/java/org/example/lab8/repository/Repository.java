package org.example.lab8.repository;

import org.example.lab8.utils.paging.Page;
import org.example.lab8.utils.paging.Pageable;

import java.util.Optional;

public interface Repository<ID, E> {
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
    Optional<E> delete(ID id);
    Optional<E> update(E entity);

    Page<E> findAllOnPage(Pageable pageable);
}
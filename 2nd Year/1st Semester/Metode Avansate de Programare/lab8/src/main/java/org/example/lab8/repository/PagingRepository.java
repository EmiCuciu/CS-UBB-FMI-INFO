package org.example.lab8.repository;

import org.example.lab8.utils.paging.Page;
import org.example.lab8.utils.paging.Pageable;

public interface PagingRepository<ID, E> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}

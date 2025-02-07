package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Entity;
import org.example.zboruri.Utils.Paging.Page;
import org.example.zboruri.Utils.Paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends IRepository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}

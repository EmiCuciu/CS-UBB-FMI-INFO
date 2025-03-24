package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.ComandaItem;

public interface IComandaItemRepository extends IRepository<Integer, ComandaItem> {
    Iterable<ComandaItem> findByComandaId(Integer comandaId);
}
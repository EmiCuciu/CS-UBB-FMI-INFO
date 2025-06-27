package com.persistence;

import com.model.Jucator;

import java.util.Optional;

public interface IRepoJucatori extends IRepository<Integer, Jucator> {
    Optional<Jucator> findByPorecla(String porecla);
}

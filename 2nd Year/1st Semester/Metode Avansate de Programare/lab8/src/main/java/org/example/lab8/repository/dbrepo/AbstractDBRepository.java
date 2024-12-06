package org.example.lab8.repository.dbrepo;


import org.example.lab8.domain.Prietenie;
import org.example.lab8.repository.Repository;
import org.example.lab8.utils.paging.Page;
import org.example.lab8.utils.paging.Pageable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public abstract class AbstractDBRepository<T> implements Repository<Long, T> {
    private String url;
    private String username;
    private String password;

    public AbstractDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public abstract Optional<T> save(T entity);
    public abstract Optional<T> delete(Long id);
    public abstract Optional<T> update(T entity);
    public abstract Iterable<T> findAll();
    public abstract Optional<T> findOne(Long id);

    public abstract Page<T> findAllOnPage(Pageable pageable);

    public abstract Page<Prietenie> findAllOnPage(Pageable pageable, Long userId);
}
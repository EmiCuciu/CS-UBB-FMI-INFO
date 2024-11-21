package org.example.lab6.repository.database;

import org.example.lab6.domain.Entity;
import org.example.lab6.domain.validation.Validator;
import org.example.lab6.repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final String url;
    private final String username;
    private final String password;
    protected final Validator<E> validator;

    public AbstractDBRepository(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    protected Connection prepareConcection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public Optional<E> findOne(ID id) {
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        return null;
    }

    @Override
    public Optional<E> save(E entity) {
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.empty();
    }
}

package org.example.lab8.domain;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
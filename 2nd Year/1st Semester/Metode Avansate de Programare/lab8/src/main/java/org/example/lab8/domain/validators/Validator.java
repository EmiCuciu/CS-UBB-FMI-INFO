package org.example.lab8.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
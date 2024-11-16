package org.example.lab6.Domain.Validations;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
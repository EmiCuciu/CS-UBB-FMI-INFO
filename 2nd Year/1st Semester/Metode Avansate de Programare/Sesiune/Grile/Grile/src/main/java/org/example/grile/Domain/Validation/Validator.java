package org.example.grile.Domain.Validation;

public interface Validator<T> {
    void validate(T entity);
}

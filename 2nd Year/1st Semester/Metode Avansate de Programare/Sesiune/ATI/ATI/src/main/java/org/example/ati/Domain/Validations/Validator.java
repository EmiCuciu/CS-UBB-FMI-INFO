package org.example.ati.Domain.Validations;

public interface Validator<T> {
    void validate(T entity) throws Exception;
}

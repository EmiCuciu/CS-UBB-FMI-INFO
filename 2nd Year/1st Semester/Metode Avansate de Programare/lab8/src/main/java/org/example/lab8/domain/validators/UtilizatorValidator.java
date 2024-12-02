package org.example.lab8.domain.validators;

import org.example.lab8.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName() == null || entity.getLastName() == null)
            throw new ValidationException("Names cannot be null");
        if(entity.getFirstName().equals("") || entity.getLastName().equals(""))
            throw new ValidationException("Names cannot be empty");
        if(entity.getFirstName().length() < 3 || entity.getLastName().length() < 3)
            throw new ValidationException("Names must have at least 3 characters");
    }
}

package org.example.lab6.Domain.Validations;

import org.example.lab6.Domain.User;

public class UserValidation implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName() == null || entity.getLastName() == null)
            throw new ValidationException("Names cannot be null");
    }
}

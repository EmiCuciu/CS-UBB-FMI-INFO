package org.example.lab6.domain.validation;

import org.example.lab6.domain.User;

public class UserValidation implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getFirstName() == null || entity.getLastName() == null)
            throw new ValidationException("Names cannot be null");
    }
}

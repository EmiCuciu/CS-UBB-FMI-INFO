package org.example.lab6.src.domain.validators;

import org.example.lab6.src.domain.User;

public class ValidatorUser implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errormsg = "";

        if (entity.getFirstName().isEmpty())
            errormsg += "Prenumele nu poate fi null";

        if (entity.getLastName().isEmpty())
            errormsg += "Numele de familie nu poate fi null";

        System.out.println(errormsg);

        if (!errormsg.isEmpty())
            throw new ValidationException(errormsg);
    }
}

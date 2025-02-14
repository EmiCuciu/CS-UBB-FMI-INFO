package org.example.lab6.src.domain.validators;

import org.example.lab6.src.domain.Prietenie;

import java.util.Objects;

public class ValidatorPrietenie implements Validator<Prietenie> {

    @Override
    public void validate(Prietenie entity) throws ValidationException {

        String errors = "";

        if (Objects.equals(entity.getIdUser1(), entity.getIdUser2()))
            errors += "TREBUIE PERSOANE DIFERITE\n";

        System.out.println(errors);

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}

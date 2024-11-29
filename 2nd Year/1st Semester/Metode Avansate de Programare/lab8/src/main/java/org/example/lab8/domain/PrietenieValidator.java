package org.example.lab8.domain;

import java.util.Objects;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String err = "";
        if (Objects.equals(entity.getId().getE1(), entity.getId().getE2()))
            err += "Nu poti fi prieten cu tine insuti!\n";
        if (!err.isEmpty())
            throw new ValidationException(err);
    }
}

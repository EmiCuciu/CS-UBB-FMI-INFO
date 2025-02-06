package org.example.grile.Domain.Validation;

import org.example.grile.Domain.Table;

public class TableValidator implements Validator<Table> {
    @Override
    public void validate(Table table){
        if(table.getId() == 0)
            throw new RuntimeException("ID-ul nu poate sa fie 0");
    }
}

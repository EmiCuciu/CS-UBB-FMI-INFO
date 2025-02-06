package org.example.grile.Domain.Validation;

import org.example.grile.Domain.MenuItem;

public class MenuItemValidator implements Validator<MenuItem> {
    @Override
    public void validate(MenuItem menuItem){
        if(menuItem.getId() == 0)
            throw new RuntimeException("ID-ul nu poate fi 0");
        if(menuItem.getCategory() == null)
            throw new RuntimeException("Categoria nu poate fi vida");
        if(menuItem.getCurrency() == null)
            throw new RuntimeException("Valuta nu poate fi vida");
        if(menuItem.getItem() == null)
            throw new RuntimeException("Felul de mancare nu poate fi vid");
        if(menuItem.getPrice() == 0)
            throw new RuntimeException("Nu poti manca pe degeaba");
    }
}

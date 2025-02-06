package org.example.grile.Domain.Validation;

import org.example.grile.Domain.Order;

public class OrderValidator implements Validator<Order> {
    @Override
    public void validate(Order order){
        if(order.getId() == 0)
            throw new RuntimeException("ID-ul nu poate fi 0");
        if(order.getDate() == null)
            throw new RuntimeException("Data nu poate fi null");
        if(order.getMenuItem() == null)
            throw new RuntimeException("Meniul nu poate fi null");
        if(order.getStatus() == null)
            throw new RuntimeException("Statusul nu poate fi null");
        if(order.getTable() == 0)
            throw new RuntimeException("Masa nu poate fi null");
    }
}

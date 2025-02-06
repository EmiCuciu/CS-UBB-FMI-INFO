package org.example.grile.Utils.Events;

import org.example.grile.Domain.Order;

public class OrderEvent implements Event{
    private ChangeEventType type;
    private Order data;

    public OrderEvent(ChangeEventType type, Order data) {
        this.type = type;
        this.data = data;
    }
}

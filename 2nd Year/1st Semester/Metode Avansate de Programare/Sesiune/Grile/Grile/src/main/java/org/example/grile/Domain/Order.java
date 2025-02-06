// Order.java
package org.example.grile.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    protected int id;
    protected int table;
    protected List<Integer> menuItem;
    protected LocalDateTime date;
    protected OrderStatus status;

    public Order(int id, int table, List<Integer> menuItem, LocalDateTime date, OrderStatus status) {
        this.id = id;
        this.table = table;
        this.menuItem = menuItem;
        this.date = date;
        this.status = status;
    }

    public Order(int table, List<Integer> menuItem, LocalDateTime date, OrderStatus status) {
        this.table = table;
        this.menuItem = menuItem;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public List<Integer> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(List<Integer> menuItem) {
        this.menuItem = menuItem;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // Add these getter methods
    public int getTableId() {
        return table;
    }

    public List<Integer> getItems() {
        return menuItem;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getId() == order.getId() && Objects.equals(getTable(), order.getTable()) && Objects.equals(getMenuItem(), order.getMenuItem()) && Objects.equals(getDate(), order.getDate()) && getStatus() == order.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTable(), getMenuItem(), getDate(), getStatus());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", table=" + table +
                ", menuItem=" + menuItem +
                ", date=" + date +
                ", status=" + status +
                '}';
    }


}
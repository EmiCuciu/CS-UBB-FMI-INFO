package org.example.grile.Service;

import org.example.grile.Domain.MenuItem;
import org.example.grile.Domain.Order;
import org.example.grile.Domain.OrderStatus;
import org.example.grile.Domain.Table;
import org.example.grile.Repository.MenuItemRepository;
import org.example.grile.Repository.OrderRepository;
import org.example.grile.Repository.TableRepository;
import org.example.grile.Utils.Events.ChangeEventType;
import org.example.grile.Utils.Events.OrderEvent;
import org.example.grile.Utils.Obs.Observable;
import org.example.grile.Utils.Obs.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<OrderEvent> {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final List<Observer<OrderEvent>> observers = new ArrayList<>();

    public Service(OrderRepository orderRepository, TableRepository tableRepository, MenuItemRepository menuItemRepository){
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void addObserver(Observer<OrderEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<OrderEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(OrderEvent event) {
        for(Observer<OrderEvent> observer : observers){
            observer.update(event);
        }
    }

    public List<Table> getAllTables(){
        return tableRepository.findAll();
    }

    public List<MenuItem> getAllMenuItems(){
        return menuItemRepository.findAll();
    }

    public void placeOrder(int tableID, List<Integer> menuItemIds) {
        if (menuItemIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order order = new Order(
                tableID,
                menuItemIds,
                LocalDateTime.now(),
                OrderStatus.PLACED
        );
        orderRepository.saveOrder(order);
        notifyObservers(new OrderEvent(ChangeEventType.ADD,order));
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAllPlaced();
    }
}

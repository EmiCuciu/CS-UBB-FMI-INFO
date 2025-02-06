package org.example.grile.GUI;

import org.example.grile.Domain.Validation.MenuItemValidator;
import org.example.grile.Domain.Validation.OrderValidator;
import org.example.grile.Domain.Validation.TableValidator;
import org.example.grile.Repository.MenuItemRepository;
import org.example.grile.Repository.OrderRepository;
import org.example.grile.Repository.TableRepository;
import org.example.grile.Service.Service;

public class Controller {
    TableValidator tableValidator;
    MenuItemValidator menuItemValidator;
    OrderValidator orderValidator;
    TableRepository tableRepository = new TableRepository("jdbc:postgresql://localhost:5432/ComenziRestaurant","postgres","emi12345",tableValidator);
    MenuItemRepository menuItemRepository = new MenuItemRepository("jdbc:postgresql://localhost:5432/ComenziRestaurant","postgres","emi12345",menuItemValidator);
    OrderRepository orderRepository = new OrderRepository("jdbc:postgresql://localhost:5432/ComenziRestaurant","postgres","emi12345",orderValidator);
    private final Service service = new Service(orderRepository, tableRepository, menuItemRepository);

    public Service getService() {
        return service;
    }
}

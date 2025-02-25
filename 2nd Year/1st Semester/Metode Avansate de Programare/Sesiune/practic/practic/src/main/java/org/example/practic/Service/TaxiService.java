package org.example.practic.Service;

import org.example.practic.Domain.Driver;
import org.example.practic.Domain.Order;
import org.example.practic.Domain.Status;
import org.example.practic.Repository.DriverRepository;
import org.example.practic.Repository.OrderRepository;
import org.example.practic.Utils.Events.ChangeEventType;
import org.example.practic.Utils.Events.CursaChangeEvent;
import org.example.practic.Utils.Events.Event;
import org.example.practic.Utils.Obs.Observable;
import org.example.practic.Utils.Obs.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;


public class TaxiService implements Observable {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final List<Observer> observers = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Integer, ScheduledFuture<?>> orderTimers = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> orderAttempts = new ConcurrentHashMap<>(); // Track reassignment attempts for each order

    public TaxiService(DriverRepository driverRepository, OrderRepository orderRepository) {
        this.driverRepository = driverRepository;
        this.orderRepository = orderRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public List<Order> getActiveOrdersForDriver(Integer driverId) {
        return orderRepository.findActiveOrdersByDriver(driverId);
    }

    public void addNewOrder(String pickupAddress, String destinationAddress, String clientName) {
        // Generate a unique ID for the order
        int orderId = generateOrderId();

        Order order = new Order(
                orderId,
                null,  // No driver assigned initially
                Status.PENDING,
                LocalDateTime.now(),
                null,
                pickupAddress,
                destinationAddress,
                clientName
        );

        orderRepository.save(order);
        orderAttempts.put(orderId, 0);  // Initialize attempt counter
        assignOrderToDriver(order);
    }

    private void assignOrderToDriver(Order order) {
        List<Driver> availableDrivers = orderRepository.findAvailableDriversSortedByLastOrder();

        // Get current attempt number
        int currentAttempt = orderAttempts.getOrDefault(order.getId(), 0);

        // Check if we have tried all available drivers
        if (currentAttempt >= availableDrivers.size()) {
            // Reset attempts and start over, or implement different logic for when all drivers have been tried
            orderAttempts.put(order.getId(), 0);
            // Could implement additional logic here (e.g., notify dispatcher, put order on hold, etc.)
            return;
        }

        // Get the next driver to try
        Driver selectedDriver = availableDrivers.get(currentAttempt);

        // Update attempt counter
        orderAttempts.put(order.getId(), currentAttempt + 1);

        // Notify the selected driver
        notifyDriver(selectedDriver, order);
    }

    private void notifyDriver(Driver driver, Order order) {
        // Create event data with both driver and order information
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("driver", driver);
        eventData.put("order", order);

        // Notify observers (driver windows) about the new order
        notifyObservers(new CursaChangeEvent(ChangeEventType.NEW_ORDER_NOTIFICATION, eventData));

        // Schedule reassignment if driver doesn't respond in 5 seconds
        ScheduledFuture<?> timer = scheduler.schedule(() -> {
            // Cancel previous timer if it exists
            ScheduledFuture<?> existingTimer = orderTimers.remove(order.getId());
            if (existingTimer != null) {
                existingTimer.cancel(false);
            }

            // Check if order is still pending
            Order currentOrder = orderRepository.findOne(order.getId());
            if (currentOrder != null && currentOrder.getStatus() == Status.PENDING) {
                // Notify about reassignment
                notifyObservers(new CursaChangeEvent(ChangeEventType.ORDER_REASSIGNED, eventData));
                // Try to assign to next driver
                assignOrderToDriver(order);
            }
        }, 5, TimeUnit.SECONDS);

        // Store the timer
        orderTimers.put(order.getId(), timer);
    }

    public void acceptOrder(Integer driverId, Order order) {
        // Cancel the reassignment timer
        ScheduledFuture<?> timer = orderTimers.remove(order.getId());
        if (timer != null) {
            timer.cancel(false);
        }

        // Remove from attempts tracking
        orderAttempts.remove(order.getId());

        // Update order status
        order.setDriverId(driverId);
        order.setStatus(Status.IN_PROGRESS);
        orderRepository.update(order);

        // Notify about acceptance
        notifyObservers(new CursaChangeEvent(ChangeEventType.ORDER_ACCEPTED, order));
    }

    public void finishOrder(Order order) {
        order.setStatus(Status.FINISHED);
        order.setEndDate(LocalDateTime.now());
        orderRepository.update(order);

        // Notify about order completion
        notifyObservers(new CursaChangeEvent(ChangeEventType.ORDER_FINISHED, order));
    }

    private Integer generateOrderId() {
        // In a production environment, you might want to use a more sophisticated ID generation strategy
        Random random = new Random();
        int newId;
        do {
            newId = random.nextInt(1000000); // Generate a random number between 0 and 999999
        } while (orderRepository.findOne(newId) != null); // Make sure ID doesn't already exist

        return newId;
    }

    // Observable implementation
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}
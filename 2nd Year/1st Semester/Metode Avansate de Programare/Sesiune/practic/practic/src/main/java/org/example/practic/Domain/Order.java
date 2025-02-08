package org.example.practic.Domain;

import java.time.LocalDateTime;

public class Order extends Entity<Integer> {
    private Integer driverId;
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String pickupAddress;
    private String destinationAddress;
    private String clientName;

    public Order(Integer id, Integer driverId, Status status, LocalDateTime startDate,
                 LocalDateTime endDate, String pickupAddress, String destinationAddress,
                 String clientName) {
        this.setId(id);
        this.driverId = driverId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.clientName = clientName;
    }

    // Getters and setters
    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (%s)", pickupAddress, destinationAddress, clientName);
    }
}
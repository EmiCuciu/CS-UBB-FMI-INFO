package org.example.Model;

import java.util.List;
import java.util.Objects;

public class Sale {
    private int id;
    private String saleDate;
    private int showId;
    private int numTickets;
    private List<Integer> seats;
    private double totalAmount;
    private SaleStatus status;

    public Sale(int id, String saleDate, int showId, int numTickets, List<Integer> seats, double totalAmount, SaleStatus status) {
        this.id = id;
        this.saleDate = saleDate;
        this.showId = showId;
        this.numTickets = numTickets;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return getId() == sale.getId() && getShowId() == sale.getShowId() && getNumTickets() == sale.getNumTickets() && Double.compare(getTotalAmount(), sale.getTotalAmount()) == 0 && Objects.equals(getSaleDate(), sale.getSaleDate()) && Objects.equals(getSeats(), sale.getSeats()) && getStatus() == sale.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSaleDate(), getShowId(), getNumTickets(), getSeats(), getTotalAmount(), getStatus());
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", saleDate='" + saleDate + '\'' +
                ", showId=" + showId +
                ", numTickets=" + numTickets +
                ", seats=" + seats +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}

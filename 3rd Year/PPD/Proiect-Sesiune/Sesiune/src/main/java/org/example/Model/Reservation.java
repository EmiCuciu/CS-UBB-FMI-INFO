package org.example.Model;

import java.util.List;
import java.util.Objects;

public class Reservation {
    private int saleId;
    private int showId;
    private List<Integer> seats;
    private int clientId;
    private long timestamp;

    public Reservation(int showId, List<Integer> seats, int clientId, long timestamp) {
        this.showId = showId;
        this.seats = seats;
        this.clientId = clientId;
        this.timestamp = timestamp;
    }

    public boolean isExpired(long maxMillis) {
        return (System.currentTimeMillis() - timestamp) > maxMillis;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return getSaleId() == that.getSaleId() && getShowId() == that.getShowId() && getClientId() == that.getClientId() && getTimestamp() == that.getTimestamp() && Objects.equals(getSeats(), that.getSeats());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSaleId(), getShowId(), getSeats(), getClientId(), getTimestamp());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "saleId=" + saleId +
                ", showId=" + showId +
                ", seats=" + seats +
                ", clientId=" + clientId +
                ", timestamp=" + timestamp +
                '}';
    }
}

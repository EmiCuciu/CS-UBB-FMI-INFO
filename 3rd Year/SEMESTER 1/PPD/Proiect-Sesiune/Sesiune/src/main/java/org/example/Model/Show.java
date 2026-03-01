package org.example.Model;

import java.util.Objects;

public class Show {
    private int id;
    private String date;
    private String title;
    private double pricePerTicket;

    public Show(int id, String date, String title, double pricePerTicket) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.pricePerTicket = pricePerTicket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return getId() == show.getId() && Double.compare(getPricePerTicket(), show.getPricePerTicket()) == 0 && Objects.equals(getDate(), show.getDate()) && Objects.equals(getTitle(), show.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTitle(), getPricePerTicket());
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", pricePerTicket=" + pricePerTicket +
                '}';
    }
}

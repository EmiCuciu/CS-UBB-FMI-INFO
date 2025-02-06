package org.example.grile.Domain;

import java.util.Objects;

public class MenuItem {
    private int id;
    private String category;
    private String item;
    private Float price;
    private String currency;        ///valuta

    public MenuItem(int id, String category, String item, Float price, String currency) {
        this.id = id;
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return getId() == menuItem.getId() && Objects.equals(getCategory(), menuItem.getCategory()) && Objects.equals(getItem(), menuItem.getItem()) && Objects.equals(getPrice(), menuItem.getPrice()) && Objects.equals(getCurrency(), menuItem.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCategory(), getItem(), getPrice(), getCurrency());
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", item='" + item + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
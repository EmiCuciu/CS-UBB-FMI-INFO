package org.example.practic.Domain;

public class Driver extends Entity<Integer> {
    private String name;

    public Driver(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Driver{id=" + getId() + ", name='" + name + "'}";
    }
}

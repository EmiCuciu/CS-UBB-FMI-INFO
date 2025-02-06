package org.example.ati_v2.Domain;

public abstract class Entity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}

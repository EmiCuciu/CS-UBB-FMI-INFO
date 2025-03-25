package com.example.lab4.model;

public interface Identifiable<Tid> {
    Tid getID();
    void setID(Tid id);
}

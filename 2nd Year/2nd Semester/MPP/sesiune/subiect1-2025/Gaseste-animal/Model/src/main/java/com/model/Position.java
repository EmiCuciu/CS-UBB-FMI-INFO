package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Position")
public class Position extends Entiti<Integer>{

    private int x;
    private int y;

    public Position() {
        super(0);
    }

    public Position(Integer id, int x, int y) {
        super(id);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + getId() +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

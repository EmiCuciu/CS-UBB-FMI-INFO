package org.example.apeleromane.Utils.Events;


import org.example.apeleromane.Domain.Rau;

public class CotaChangeEvent extends Event {
    private Rau rau;

    public CotaChangeEvent(ChangeEventType type, Rau rau) {
        super(type, rau);
        this.rau = rau;
    }

    public Rau getRau() {
        return rau;
    }
}

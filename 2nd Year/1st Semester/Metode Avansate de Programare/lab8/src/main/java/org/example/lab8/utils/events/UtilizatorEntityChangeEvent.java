package org.example.lab8.utils.events;


import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.Utilizator;

public class UtilizatorEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Utilizator data, oldData;
    private Prietenie prietenie;

    public UtilizatorEntityChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
        
    }
    public UtilizatorEntityChangeEvent(ChangeEventType type, Utilizator data, Utilizator oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public UtilizatorEntityChangeEvent(ChangeEventType changeEventType, Prietenie prietenie) {
        this.type = changeEventType;
        this.prietenie = prietenie;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}
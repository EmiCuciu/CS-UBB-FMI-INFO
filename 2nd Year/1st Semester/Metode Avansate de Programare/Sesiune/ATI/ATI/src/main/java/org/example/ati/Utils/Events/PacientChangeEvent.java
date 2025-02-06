package org.example.ati.Utils.Events;

import org.example.ati.Domain.Pacient;

public class PacientChangeEvent extends Event {
    private Pacient pacient;

    public PacientChangeEvent(ChangeEventType type, Pacient pacient) {
        super(type, pacient);
        this.pacient = pacient;
    }

    public Pacient getPacient() {
        return pacient;
    }
}

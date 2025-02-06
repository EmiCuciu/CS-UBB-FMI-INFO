package org.example.ati_v2.Utils.Events;

import org.example.ati_v2.Domain.Pacient;

public class PacientChangeEvent extends Event{
    private Pacient pacient;

    public PacientChangeEvent(ChangeEventType type, Pacient pacient){
        super(type, pacient);
        this.pacient = pacient;
    }

    public Pacient getPacient() {
        return pacient;
    }
}

package com.example.laboratoriss.Domain;

import jakarta.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class Entiti<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    public Entiti() {
        // Default constructor required by Hibernate
    }

    public Entiti(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
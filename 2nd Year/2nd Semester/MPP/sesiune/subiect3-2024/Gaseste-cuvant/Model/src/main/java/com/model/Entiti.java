package com.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public class Entiti<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

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
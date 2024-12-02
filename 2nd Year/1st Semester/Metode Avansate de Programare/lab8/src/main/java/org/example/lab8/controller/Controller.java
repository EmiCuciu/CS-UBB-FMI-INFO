package org.example.lab8.controller;


import org.example.lab8.domain.validators.MessageValidator;
import org.example.lab8.domain.validators.PrietenieValidator;
import org.example.lab8.domain.validators.UtilizatorValidator;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.repository.dbrepo.UserDBRepository;
import org.example.lab8.services.Service;

public class Controller {
    UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();
    MessageValidator messageValidator = new MessageValidator();
    MessageDBRepository messageDBRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", messageValidator);
    private final Service service = new Service(new UserDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", utilizatorValidator), new PrietenieDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", prietenieValidator), messageDBRepository);

    public Service getService() {
        return service;
    }
}
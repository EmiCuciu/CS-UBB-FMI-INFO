package com.example.guiex1.controller;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.dbrepo.PrietenieDBRepository;
import com.example.guiex1.repository.dbrepo.UserDBRepository;
import com.example.guiex1.services.Service;

import java.util.Set;

public class Controller {
    UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();
    private Service service = new Service(new UserDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", utilizatorValidator), new PrietenieDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", prietenieValidator));

    public UtilizatorValidator getUtilizatorValidator() {
        return utilizatorValidator;
    }

    public void setUtilizatorValidator(UtilizatorValidator utilizatorValidator) {
        this.utilizatorValidator = utilizatorValidator;
    }

    public PrietenieValidator getPrietenieValidator() {
        return prietenieValidator;
    }

    public void setPrietenieValidator(PrietenieValidator prietenieValidator) {
        this.prietenieValidator = prietenieValidator;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void addFriendRequest(Long userId, Long friendId) {
        service.addFriendRequest(userId, friendId);
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        service.acceptFriendRequest(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        service.removeFriend(userId, friendId);
    }

    public Set<Utilizator> findFriends(Long userId) {
        return service.findFriends(userId);
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        return service.findFriendRequests(userId);
    }
}
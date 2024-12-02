package org.example.lab8.controller;


import org.example.lab8.domain.*;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.repository.dbrepo.UserDBRepository;
import org.example.lab8.services.Service;

import java.util.List;
import java.util.Set;

public class Controller {
    UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();
    MessageDBRepository messageDBRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345");
    private Service service = new Service(new UserDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", utilizatorValidator), new PrietenieDBRepository("jdbc:postgresql://localhost:5432/ExempluLab6DB", "postgres", "emi12345", prietenieValidator), messageDBRepository);


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void addFriendRequest(Long userId, Long friendId) {
        service.getFriendshipService().addFriendRequest(userId, friendId);
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        service.getFriendshipService().acceptFriendRequest(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        service.getFriendshipService().removeFriend(userId, friendId);
    }

    public Set<Utilizator> findFriends(Long userId) {
        return service.getFriendshipService().findFriends(userId);
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        return service.getFriendshipService().findFriendRequests(userId);
    }

    public List<Message> getMessages(Long id, Long id1) {
        return service.getMessageService().getMessages(id, id1);
    }

    public void sendMessage(Message message) {
        messageDBRepository.save(message);
    }

}
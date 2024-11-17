package com.example.guiex1.controller;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.Service;

import java.util.Set;

public class Controller {
    private Service service;

    public void setUtilizatorService(Service service) {
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
package org.example.lab8.services;


import org.example.lab8.domain.*;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.repository.dbrepo.UserDBRepository;
import org.example.lab8.utils.events.ChangeEventType;
import org.example.lab8.utils.events.UtilizatorEntityChangeEvent;
import org.example.lab8.utils.observer.Observable;
import org.example.lab8.utils.observer.Observer;

import java.util.*;

public class Service implements Observable<UtilizatorEntityChangeEvent> {
    UtilizatorValidator validator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();
    private final UserDBRepository userDBRepository;
    private final PrietenieDBRepository prietenieDBRepository;
    private final MessageDBRepository messageDBRepository;

    private final List<Observer<UtilizatorEntityChangeEvent>> observers = new ArrayList<>();

    public Service(UserDBRepository userDBRepository, PrietenieDBRepository prietenieDBRepository, MessageDBRepository messageDBRepository) {
        this.userDBRepository = userDBRepository;
        this.prietenieDBRepository = prietenieDBRepository;
        this.messageDBRepository = messageDBRepository;
    }


    public Utilizator addUtilizator(Utilizator user) {
        if (userDBRepository.save(user).isPresent()) {
            return user;
        }
        notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user));
        return null;
    }

    public Utilizator deleteUtilizator(Long id) {
        Optional<Utilizator> user = userDBRepository.delete(id);
        user.ifPresent(u -> notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, u)));
        return user.orElse(null);
    }

    public Iterable<Utilizator> getAll() {
        return userDBRepository.findAll();
    }

    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {
        for (Observer<UtilizatorEntityChangeEvent> observer : observers) {
            observer.update(t);
        }    }

    public Utilizator updateUtilizator(Utilizator u) {
        Optional<Utilizator> oldUser = userDBRepository.findOne(u.getId());
        if (oldUser.isPresent()) {
            Optional<Utilizator> newUser = userDBRepository.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }


    public Set<Utilizator> findFriends(Long userId) {
        return prietenieDBRepository.findFriends(userId);
    }

    public void addFriendRequest(Long userId, Long friendId) {
        prietenieDBRepository.addFriendRequest(userId, friendId);
        notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.ADD, userDBRepository.findById(friendId).orElse(null)));
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        prietenieDBRepository.acceptFriendRequest(userId, friendId);
    }



    public void removeFriend(Long userId, Long friendId) {
        prietenieDBRepository.removeFriend(userId, friendId);
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        return prietenieDBRepository.findFriendRequests(userId);
    }

    public Optional<Utilizator> findByUsernameAndPassword(String username, String password) {
        return userDBRepository.findByUsernameAndPassword(username, password);
    }

    public void saveUser(Utilizator user) {
        userDBRepository.save(user);
    }

    public HashSet<Utilizator> findAllUsers() {
        return (HashSet<Utilizator>) userDBRepository.findAll();
    }

    public Optional<Utilizator> findUserById(Long id) {
        return userDBRepository.findById(id);
    }


    public Utilizator findUtilizatorByUsername(String logedInUsername) {
        return userDBRepository.findUtilizatorByUsername(logedInUsername);
    }

    public void sendMessage(Message message) {
        messageDBRepository.save(message);
    }

    public List<Message> getMessages(Long userId1, Long userId2) {
        return messageDBRepository.getMessages(userId1, userId2);
    }


}
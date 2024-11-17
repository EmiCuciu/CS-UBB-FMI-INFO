package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.dbrepo.DBRepository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Service implements Observable<UtilizatorEntityChangeEvent> {
    private com.example.guiex1.repository.Repository<Long, Utilizator> repo;
    private List<Observer<UtilizatorEntityChangeEvent>> observers = new ArrayList<>();

    public Service(com.example.guiex1.repository.Repository<Long, Utilizator> repo) {
        this.repo = repo;
    }

    public Utilizator addUtilizator(Utilizator user) {
        if (repo.save(user).isEmpty()) {
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public Utilizator deleteUtilizator(Long id) {
        Optional<Utilizator> user = repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();
        }
        return null;
    }

    public Iterable<Utilizator> getAll() {
        return repo.findAll();
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
        observers.forEach(x -> x.update(t));
    }

    public Utilizator updateUtilizator(Utilizator u) {
        Optional<Utilizator> oldUser = repo.findOne(u.getId());
        if (oldUser.isPresent()) {
            Optional<Utilizator> newUser = repo.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }

    public void addFriend(Long userId, Long friendId) {
        ((DBRepository) repo).addFriendRequest(userId, friendId);
    }

    public Set<Utilizator> findFriends(Long userId) {
        return ((DBRepository) repo).findFriends(userId);
    }

    public void addFriendRequest(Long userId, Long friendId) {
        ((DBRepository) repo).addFriendRequest(userId, friendId);
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        ((DBRepository) repo).acceptFriendRequest(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        ((DBRepository) repo).removeFriend(userId, friendId);
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        return ((DBRepository) repo).findFriendRequests(userId);
    }

    public Optional<Utilizator> findByUsernameAndPassword(String username, String password) {
        return ((DBRepository) repo).findByUsernameAndPassword(username, password);
    }

    public void saveUser(Utilizator user) {
        ((DBRepository) repo).saveUser(user);
    }
}
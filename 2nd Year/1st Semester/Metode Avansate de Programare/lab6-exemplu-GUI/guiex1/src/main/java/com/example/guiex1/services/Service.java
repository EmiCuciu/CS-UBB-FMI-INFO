package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.dbrepo.PrietenieDBRepository;
import com.example.guiex1.repository.dbrepo.UserDBRepository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Service implements Observable<UtilizatorEntityChangeEvent> {
    UtilizatorValidator validator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();
    private final UserDBRepository userDBRepository;
    private final PrietenieDBRepository prietenieDBRepository;

    private final List<Observer<UtilizatorEntityChangeEvent>> observers = new ArrayList<>();

    public Service(UserDBRepository userDBRepository, PrietenieDBRepository prietenieDBRepository) {
        this.userDBRepository = userDBRepository;
        this.prietenieDBRepository = prietenieDBRepository;
    }

    public Utilizator addUtilizator(Utilizator user) {
        if (userDBRepository.save(user).isEmpty()) {
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public Utilizator deleteUtilizator(Long id) {
        Optional<Utilizator> user = userDBRepository.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();
        }
        return null;
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
        observers.forEach(x -> x.update(t));
    }

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

    public void addFriend(Long userId, Long friendId) {
        prietenieDBRepository.addFriendRequest(userId, friendId);
    }

    public Set<Utilizator> findFriends(Long userId) {
        return prietenieDBRepository.findFriends(userId);
    }

    public void addFriendRequest(Long userId, Long friendId) {
        prietenieDBRepository.addFriendRequest(userId, friendId);
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
}
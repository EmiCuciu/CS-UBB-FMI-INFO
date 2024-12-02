package org.example.lab8.services;

import org.example.lab8.domain.Utilizator;
import org.example.lab8.repository.dbrepo.UserDBRepository;
import org.example.lab8.utils.events.ChangeEventType;
import org.example.lab8.utils.events.UserEvent;
import org.example.lab8.utils.observer.Observable;
import org.example.lab8.utils.observer.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserService implements Observable<UserEvent> {
    private final UserDBRepository userDBRepository;
    private final List<Observer<UserEvent>> observers = new ArrayList<>();

    public UserService(UserDBRepository userDBRepository) {
        this.userDBRepository = userDBRepository;
    }

    public Utilizator addUtilizator(Utilizator u) {
        Optional<Utilizator> user = userDBRepository.save(u);
        user.ifPresent(u1 -> notifyObservers(new UserEvent(ChangeEventType.ADD, u1)));
        return user.orElse(null);
    }

    public Utilizator deleteUtilizator(Long id) {
        Optional<Utilizator> user = userDBRepository.delete(id);
        user.ifPresent(u -> notifyObservers(new UserEvent(ChangeEventType.DELETE, u)));
        return user.orElse(null);
    }

    public Iterable<Utilizator> getAll() {
        return userDBRepository.findAll();
    }

    @Override
    public void addObserver(Observer<UserEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEvent t) {
        observers.forEach(o -> o.update(t));
    }


    public Utilizator updateUtilizator(Utilizator u) {
        Optional<Utilizator> oldUser = userDBRepository.findOne(u.getId());
        if (oldUser.isPresent()) {
            Optional<Utilizator> newUser = userDBRepository.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UserEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }

    public Optional<Utilizator> findByUsernameAndPassword(String username, String password) {
        return userDBRepository.findByUsernameAndPassword(username, password);
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
}
package org.example.lab6.service;

import org.example.lab6.Utils.events.ChangeEventType;
import org.example.lab6.Utils.events.UserEntityChangeEvent;
import org.example.lab6.Utils.observer.Observable;
import org.example.lab6.Utils.observer.Observer;
import org.example.lab6.domain.User;
import org.example.lab6.domain.UserInstance;
import org.example.lab6.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


public class UserService implements EntityService<Long, User>,
        Observable<UserEntityChangeEvent> {

    private static UserService instance;
    private Repository<Long,User> repository;
    private final List<Observer<UserEntityChangeEvent>> observers = new ArrayList<>();

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            // Call to the parent class's getInstance
            instance = new UserService();
        }
        return instance;
    }

    public void setRepository(Repository<Long, User> repository) {
        this.repository = repository;
    }

    public Optional<User> findByUsername(String username) {
        return StreamSupport.stream(getAll().spliterator(), false).filter(user -> user.getUsername().equals(username)).findFirst();
    }
    public boolean login(String username, String password) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            if(password.equals(user.get().getPassword())) {
                UserInstance.getInstance().setId(user.get().getId());
                notifyObservers(new UserEntityChangeEvent(ChangeEventType.LOGIN));
                return true;
            }
        }
        return false;

    }
    @Override
    public Repository<Long, User> getRepo() {
        return repository;
    }

    @Override
    public Optional<User> create(User entity) {
        if(EntityService.super.create(entity).isPresent()){
            notifyObservers( new UserEntityChangeEvent(ChangeEventType.ADD,entity));
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        Optional<User> oldUser = repository.findOne(entity.getId());
        if(oldUser.isPresent()){
            Optional<User> newUser = repository.update(entity);
            if(newUser.isPresent()){
                notifyObservers(new UserEntityChangeEvent(ChangeEventType.UPDATE,entity,oldUser.get()));
            }
            return newUser;
        }
        return oldUser;
    }
    public Optional<User> getByUsername(String username) {
        return StreamSupport.stream(repository.findAll().spliterator(),false).filter(user -> user.getUsername().equals(username)).findFirst();

    }
    @Override
    public Optional<User> delete(Long entityId) {
        Optional<User> user = repository.delete(entityId);
        user.ifPresent(value -> notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, value)));
        return user;
    }

    @Override
    public Iterable<User> getAll() {
        return EntityService.super.getAll();
    }

    @Override
    public Optional<User> getById(Long entityId) {
        return EntityService.super.getById(entityId);
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {
        observers.forEach(x->x.update(t));
    }

    public void addUser(User validUser) {
        repository.save(validUser);
    }
}

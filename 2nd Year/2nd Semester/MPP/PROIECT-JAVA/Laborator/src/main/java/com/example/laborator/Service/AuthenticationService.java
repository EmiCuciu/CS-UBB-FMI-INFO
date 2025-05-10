package com.example.laborator.Service;

import com.example.laborator.Domain.Arbitru;
import com.example.laborator.Repository.IArbitruRepository;
import com.example.laborator.Utils.Observable;
import com.example.laborator.Utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService extends Observable {
    private final IArbitruRepository repository;
    private final List<Observer> observers = new ArrayList<>();
    private Arbitru currentUser;

    public AuthenticationService(IArbitruRepository repository) {
        this.repository = repository;
    }

    public Arbitru login(String username, String password) {
        Iterable<Arbitru> arbitri = repository.findAll();
        for (Arbitru arbitru : arbitri) {
            if (arbitru.getUsername().equals(username) && arbitru.getPassword().equals(password)) {
                currentUser = arbitru;
                notifyObservers();
                return arbitru;
            }
        }
        return null;
    }

    public boolean register(String username, String password, String firstName, String lastName) {
        // Check if username already exists
        Iterable<Arbitru> arbitri = repository.findAll();
        for (Arbitru arbitru : arbitri) {
            if (arbitru.getUsername().equals(username)) {
                return false; // Username already taken
            }
        }

        // Create new arbitru and save to repository
        Arbitru newArbitru = new Arbitru(0, username, password, firstName, lastName);
        repository.save(newArbitru);
        return true;
    }

    public void logout() {
        currentUser = null;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
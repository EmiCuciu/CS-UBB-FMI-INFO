package com.example.laboratoriss.Service;

import com.example.laboratoriss.Utils.Observer.ChangeEventType;
import com.example.laboratoriss.Utils.Observer.Observable;
import com.example.laboratoriss.Utils.Observer.ObservableEvent;
import com.example.laboratoriss.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<E> implements Observable<E> {
    private final List<Observer<E>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<E> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent<E> event) {
        observers.forEach(observer -> observer.update(event));
    }

    protected void notifyAdd(E entity) {
        notifyObservers(new ObservableEvent<>(this, entity, ChangeEventType.ADD));
    }

    protected void notifyUpdate(E entity) {
        notifyObservers(new ObservableEvent<>(this, entity, ChangeEventType.UPDATE));
    }

    protected void notifyDelete(E entity) {
        notifyObservers(new ObservableEvent<>(this, entity, ChangeEventType.DELETE));
    }
}
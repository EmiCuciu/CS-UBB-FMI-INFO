package com.example.laboratoriss.Utils.Observer;

public interface Observable<E> {
    void addObserver(Observer<E> observer);

    void removeObserver(Observer<E> observer);

    void notifyObservers(ObservableEvent<E> event);
}
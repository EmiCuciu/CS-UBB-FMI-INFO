package com.example.laboratoriss.Utils.Observer;

public interface Observer<E> {
    void update(ObservableEvent<E> event);
}
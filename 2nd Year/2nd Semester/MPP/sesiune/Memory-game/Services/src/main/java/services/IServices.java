package services;


import domain.Jucator;

import java.util.List;

public interface IServices {
    void logIn(Jucator jucator, IObserver observer);
    void logOut(Jucator jucator, IObserver observer);
}

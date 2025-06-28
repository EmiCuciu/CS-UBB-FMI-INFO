package services;

import domain.Joc;
import domain.Jucator;
import domain.RaspunsJucator;

import java.util.List;

public interface IServices {
    void logIn(Jucator jucator, IObserver observer);
    void logOut(Jucator jucator, IObserver observer);

    List<Joc> getJocuriByJucator(Jucator jucator);
    List<RaspunsJucator> getRaspunsuriByJoc(Joc joc);
}
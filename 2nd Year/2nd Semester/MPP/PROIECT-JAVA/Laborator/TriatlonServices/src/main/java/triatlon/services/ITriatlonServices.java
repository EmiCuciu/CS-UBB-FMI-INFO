package triatlon.services;

import triatlon.model.*;

import java.util.List;
import java.util.Map;

public interface ITriatlonServices {

    // Authentification
    void login(Arbitru arbitru, ITriatlonObserver iTriatlonObserver) throws TriatlonException;

    boolean register(String username, String password, String firstName, String lastName) throws TriatlonException;

    void logout(Arbitru arbitru, ITriatlonObserver iTriatlonObserver) throws TriatlonException;


    // Rezultat Service
    void addRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) throws TriatlonException;

    List<Rezultat> getResultateForProba(TipProba proba) throws TriatlonException;

    List<Rezultat> getAllResults();

    // Participant Service

    List<Participant> getAllParticipants() throws TriatlonException;

    int calculateTotalScore(Participant participant) throws TriatlonException;

    // Proba Service

    List<Proba> getAllProbe() throws TriatlonException;

    TipProba getProbaForArbitru(Arbitru arbitru) throws TriatlonException;

    Arbitru findArbitruByUsernameAndPassword(String username, String password);
}
package triatlon.services;

import triatlon.model.Arbitru;
import triatlon.model.Rezultat;

public interface ITriatlonObserver {
    void arbitruLoggedIn(Arbitru arbitru) throws TriatlonException;
    void arbitruLoggedOut(Arbitru arbitru) throws TriatlonException;
    void rezultatAdded(Rezultat rezultat) throws TriatlonException;
}

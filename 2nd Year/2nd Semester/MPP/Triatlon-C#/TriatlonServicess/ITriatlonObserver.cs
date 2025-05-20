using TriatlonModel;

namespace TriatlonServicess;

public interface ITriatlonObserver
{
    void ArbitruLoggedIn(Arbitru arbitru);
    void ArbitruLoggedOut(Arbitru arbitru);
    void RezultatAdded(Rezultat rezultat);
}
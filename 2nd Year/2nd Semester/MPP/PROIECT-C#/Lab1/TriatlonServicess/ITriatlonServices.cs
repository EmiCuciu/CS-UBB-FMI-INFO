using TriatlonModel;

namespace TriatlonServicess;

public interface ITriatlonServices
{
// Authentification
    void Login(Arbitru arbitru, ITriatlonObserver iTriatlonObserver);

    bool Register(String username, String password, String firstName, String lastName);

    void Logout(Arbitru arbitru, ITriatlonObserver iTriatlonObserver);


    // Rezultat Service
    void AddRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj);

    List<Rezultat> GetResultateForProba(TipProba proba);

    List<Rezultat> GetAllResults();

    // Participant Service

    List<Participant> GetAllParticipants();

    int CalculateTotalScore(Participant participant);

    // Proba Service

    List<Proba> GetAllProbe();

    TipProba GetProbaForArbitru(Arbitru arbitru);

    Arbitru FindArbitruByUsernameAndPassword(String username, String password);
}
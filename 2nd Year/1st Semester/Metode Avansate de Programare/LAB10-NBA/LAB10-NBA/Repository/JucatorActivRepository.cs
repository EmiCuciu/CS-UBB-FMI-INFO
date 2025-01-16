using LAB10_NBA.Domain;
using LAB10_NBA.Utils;

namespace LAB10_NBA.Repository;

public class JucatorActivRepository : IRepository<int, JucatorActiv>
{
    private List<JucatorActiv> _jucatorActivi = new List<JucatorActiv>();

    public JucatorActiv? Save(JucatorActiv jucatorActiv)
    {
        _jucatorActivi.Add(jucatorActiv);
        return null;
    }

    public JucatorActiv? Delete(int id)
    {
        _jucatorActivi.RemoveAll(ja => ja.IdJucator == id);
        return null;
    }

    public JucatorActiv? Update(JucatorActiv jucatorActiv)
    {
        var existingJucatorActiv = _jucatorActivi.FirstOrDefault(ja => ja.IdJucator == jucatorActiv.IdJucator);

        if (existingJucatorActiv != null)
        {
            existingJucatorActiv.IdEchipa = jucatorActiv.IdEchipa;
            existingJucatorActiv.IdMeci = jucatorActiv.IdMeci;
            existingJucatorActiv.NrPuncteInscrise = jucatorActiv.NrPuncteInscrise;
            existingJucatorActiv.Tip = jucatorActiv.Tip;
        }
        
        return existingJucatorActiv;
    }
    
    public JucatorActiv? FindOne(int id)
    {
        return _jucatorActivi.FirstOrDefault(ja => ja.IdJucator == id);
    }
    
    public List<JucatorActiv> FindAll()
    {
        return _jucatorActivi;
    }
    
    public void GenerateJucatoriActivi(
        List<Meci> meciuri,
        List<Jucator> jucatori,
        int nrPlayersPerTeam = 4
    )
    {
        foreach (var meci in meciuri)
        {
            var playersTeam1 = jucatori.Where(j => j.Echipa != null && j.Echipa.Id == meci.EchipaGazda.Id).ToList();
            var playersTeam2 = jucatori.Where(j => j.Echipa != null && j.Echipa.Id == meci.EchipaOaspete.Id).ToList();

            var selectedTeam1 = RandomUtils.GetRandomItems(playersTeam1, nrPlayersPerTeam);
            var selectedTeam2 = RandomUtils.GetRandomItems(playersTeam2, nrPlayersPerTeam);

            AssignRolesAndAddJucatoriActivi(selectedTeam1, meci.Id);
            AssignRolesAndAddJucatoriActivi(selectedTeam2, meci.Id);
        }
    }
    
    private void AssignRolesAndAddJucatoriActivi(List<Jucator> selectedPlayers, int matchId)
    {
        // Randomly pick one player to be "Rezerva"
        var rezervaIndex = RandomUtils.GetRandomNumber(0, selectedPlayers.Count - 1);

        for (var i = 0; i < selectedPlayers.Count; i++)
        {
            var jucator = selectedPlayers[i];
            var jucatorActiv = new JucatorActiv
            {
                IdJucator = jucator.Id,
                IdMeci = matchId,
                IdEchipa = jucator.Echipa?.Id ?? 0,
                NrPuncteInscrise = RandomUtils.GetRandomNumber(0, 30),
                Tip = i == rezervaIndex ? TipJucator.Rezerva : TipJucator.Participant
            };

            _jucatorActivi.Add(jucatorActiv);
        }
    }
    
    public void SaveToCsv(string filePath)
    {
        try
        {
            using var writer = new StreamWriter(filePath);
            writer.WriteLine("idJucator,idMeci,idEchipa,nrPuncteInscrise,tip");

            foreach (var jucatorActiv in _jucatorActivi)
            {
                var line =
                    $"{jucatorActiv.IdJucator},{jucatorActiv.IdMeci},{jucatorActiv.IdEchipa},{jucatorActiv.NrPuncteInscrise},{jucatorActiv.Tip}";
                writer.WriteLine(line);
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }
}
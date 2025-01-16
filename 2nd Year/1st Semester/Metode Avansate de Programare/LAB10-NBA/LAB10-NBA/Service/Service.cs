using LAB10_NBA.Domain;
using LAB10_NBA.Repository;

namespace LAB10_NBA.Service;

public class Service
{
    private EchipaRepository _echipaRepository;
    private JucatorRepository _jucatorRepository;
    private JucatorActivRepository _jucatorActivRepository;
    private MeciRepository _meciRepository;
    
    public Service(EchipaRepository echipaRepository, JucatorRepository jucatorRepository, JucatorActivRepository jucatorActivRepository, MeciRepository meciRepository)
    {
        _echipaRepository = echipaRepository;
        _jucatorRepository = jucatorRepository;
        _jucatorActivRepository = jucatorActivRepository;
        _meciRepository = meciRepository;
    }

    public List<Jucator> GetJucatoriEchipa(int idEchipa)
    {
        return _jucatorRepository.FindAll()
            .Where(j => j.Echipa != null && j.Echipa.Id == idEchipa)
            .ToList();
    }
    
    public List<Jucator> GetJucatoriActiviEchipaMeci(int idEchipa, int idMeci)
    {
        var jucatoriActivi = _jucatorActivRepository.FindAll()
            .Where(ja => ja.IdMeci == idMeci)
            .Select(ja => ja.IdJucator)
            .ToList();

        return _jucatorRepository.FindAll()
            .Where(j => j.Echipa != null && j.Echipa.Id == idEchipa && jucatoriActivi.Contains(j.Id))
            .ToList();
    }
    
    public List<Meci> GetMeciuriPerioadaCalendaristica(DateTime dataStart, DateTime dataSfarsit)
    {
        return (from meci in _meciRepository.FindAll()
            where meci.Data >= dataStart && meci.Data <= dataSfarsit
            select meci).ToList();
    }
    
    public string GetScorMeci(int idMeci)
    {
        Meci? meci = _meciRepository.FindAll().FirstOrDefault(m => m.Id == idMeci);
        if (meci == null)
        {
            throw new ArgumentException("Meciul nu a fost gasit.");
        }

        var echipaGazda = meci.EchipaGazda;
        var echipaOaspete = meci.EchipaOaspete;

        var scoruri = _jucatorActivRepository.FindAll()
            .Where(ja => ja.IdMeci == idMeci)
            .GroupBy(ja => ja.IdEchipa)
            .Select(g => new
            {
                EchipaId = g.Key,
                Scor = g.Sum(ja => ja.NrPuncteInscrise)
            })
            .ToDictionary(x => x.EchipaId, x => x.Scor);

        int scorGazda = scoruri.ContainsKey(echipaGazda.Id) ? scoruri[echipaGazda.Id] : 0;
        int scorOaspete = scoruri.ContainsKey(echipaOaspete.Id) ? scoruri[echipaOaspete.Id] : 0;


            
        return $"{echipaGazda.Nume} {scorGazda} - {scorOaspete} {echipaOaspete.Nume}";
    }

    public List<Meci> GetMeciuriForTeam(int idEchipa)
    {
        return (from meci in _meciRepository.FindAll()
            where meci.EchipaGazda.Id == idEchipa || meci.EchipaOaspete.Id == idEchipa
            select meci).ToList();
    }
}
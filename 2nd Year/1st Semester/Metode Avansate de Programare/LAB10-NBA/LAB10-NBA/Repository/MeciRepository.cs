using LAB10_NBA.Domain;

namespace LAB10_NBA.Repository;

public class MeciRepository : IRepository<int, Meci>
{
    private List<Meci> _meciuri = new List<Meci>();
    
    public void LoadFromCsv(string filePath, EchipaRepository echipaRepository)
    {
        try
        {
            using (var reader = new StreamReader(filePath))
            {
                reader.ReadLine();
                while (!reader.EndOfStream)
                {
                    var line = reader.ReadLine();
                    if(line == null)
                        continue;

                    var values = line.Split(',');
                    var idGazda = int.Parse(values[1]);
                    var idOaspete = int.Parse(values[2]);
                    var data = DateTime.Parse(values[3]);
                    Echipa echipaGazda = echipaRepository.FindOne(idGazda) ?? throw new InvalidOperationException($"Team with ID {idGazda} not found.");
                    Echipa echipaOaspete = echipaRepository.FindOne(idOaspete) ?? throw new InvalidOperationException($"Team with ID {idOaspete} not found.");

                    Meci meci = new Meci
                    {
                        Id = int.Parse(values[0]),
                        EchipaGazda = echipaGazda,
                        EchipaOaspete = echipaOaspete,
                        Data = data
                    };
                    Save(meci);
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    public Meci? Save(Meci meci)
    {
        _meciuri.Add(meci);
        return null;
    }

    public Meci? Delete(int id)
    {
        _meciuri.RemoveAll(m => m.Id == id);
        return null;
    }

    public Meci? Update(Meci meci)
    {
        var existingMeci = _meciuri.FirstOrDefault(m => m.Id == meci.Id);
        if (existingMeci != null)
        {
            existingMeci.EchipaGazda = meci.EchipaGazda;
            existingMeci.EchipaOaspete = meci.EchipaOaspete;
            existingMeci.Data = meci.Data;
        }

        return existingMeci;
    }

    public Meci? FindOne(int id)
    {
        return _meciuri.FirstOrDefault(m => m.Id == id);
    }

    public List<Meci> FindAll()
    {
        return _meciuri;
    }
}
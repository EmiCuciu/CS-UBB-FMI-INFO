namespace LAB10_NBA.Repository;

using LAB10_NBA.Domain;

public class EchipaRepository : IRepository<int, Echipa>
{
    private List<Echipa> _echipe = new List<Echipa>();

    public void LoadFromCsv(string filePath)
    {
        try
        {
            using (var reader = new StreamReader(filePath))
            {
                reader.ReadLine();

                while (!reader.EndOfStream)
                {
                    var line = reader.ReadLine();
                    var values = line?.Split(',') ?? Array.Empty<string>();

                    var echipa = new Echipa
                    {
                        Id = int.Parse(values[0]),
                        Nume = values[1]
                    };

                    Save(echipa);
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine($"Error reading CSV file for EchipaRepository : {e.Message}");
            throw;
        }
    }

    public Echipa? Save(Echipa echipa)
    {
        _echipe.Add(echipa);
        return null;
    }
    
    public Echipa? Delete(int id)
    {
        _echipe.RemoveAll(e => e.Id == id);
        return null;
    }

    public Echipa? Update(Echipa echipa)
    {
        var existingEchipa = _echipe.FirstOrDefault(e => e.Id == echipa.Id);
        if (existingEchipa != null)
        {
            existingEchipa.Nume = echipa.Nume;
        }
        
        return existingEchipa;
    }

    public Echipa? FindOne(int id)
    {
        return _echipe.FirstOrDefault(e => e.Id == id);
    }

    public List<Echipa> FindAll()
    {
        return _echipe;
    }
}
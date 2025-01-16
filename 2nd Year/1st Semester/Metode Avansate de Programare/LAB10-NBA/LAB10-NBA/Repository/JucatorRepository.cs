using LAB10_NBA.Domain;

namespace LAB10_NBA.Repository;

public class JucatorRepository : IRepository<int, Jucator>
{
    private List<Jucator> _jucatori = new List<Jucator>();

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
                    if (line == null) continue;

                    var values = line.Split(',');

                    int studentID = int.Parse(values[0]);
                    string nume = values[1];
                    string scoala = values[2];
                    int idScoala = int.Parse(values[3]);

                    Echipa echipa = echipaRepository.FindOne(idScoala);

                    if (echipa == null)
                    {
                        Console.WriteLine($"Error: Team with ID {idScoala} not found.");
                        continue;
                    }

                    var jucator = new Jucator
                    {
                        Id = studentID,
                        Nume = nume,
                        Scoala = scoala,
                        Echipa = echipa
                    };

                    _jucatori.Add(jucator);
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine($"Error loading data for JucatorRepository from {filePath}: {e.Message}");
        }
    }

    public Jucator? Save(Jucator jucator)
    {
        _jucatori.Add(jucator);
        return null;
    }

    public Jucator? Delete(int id)
    {
        _jucatori.RemoveAll(e => e.Id == id);
        return null;
    }

    public Jucator? Update(Jucator jucator)
    {
        var existingJucator = _jucatori.FirstOrDefault(j => j.Id == jucator.Id);
        if (existingJucator != null)
        {
            existingJucator.Echipa = jucator.Echipa;
            existingJucator.Nume = jucator.Nume;
            existingJucator.Scoala = jucator.Scoala;
        }

        return existingJucator;
    }

    public Jucator? FindOne(int id)
    {
        return _jucatori.FirstOrDefault(j => j.Id == id);
    }

    public List<Jucator> FindAll()
    {
        return _jucatori;
    }
}
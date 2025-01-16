namespace LAB10_NBA.Domain;

public class Meci
{
    public int Id
    {
        get;
        set;
    }

    public Echipa EchipaGazda
    {
        get;
        set;
    } = null!;

    public Echipa EchipaOaspete
    {
        get;
        set;
    } = null!;

    public DateTime Data
    {
        get;
        set;
    }

    public override string ToString()
    {
        return $"{Id}. {EchipaGazda.Nume} vs {EchipaOaspete.Nume} ({Data})";
    }
}
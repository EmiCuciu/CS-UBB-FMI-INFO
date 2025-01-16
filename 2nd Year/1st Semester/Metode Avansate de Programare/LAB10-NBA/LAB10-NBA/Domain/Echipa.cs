namespace LAB10_NBA.Domain;

public class Echipa
{
    public int Id
    {
        get; set;
    }

    public string? Nume
    {
        get;
        set;
    }

    public override string ToString()
    {
        return $"ID: {Id} ({Nume})";
    }
}
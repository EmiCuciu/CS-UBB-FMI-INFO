namespace LAB10_NBA.Domain;

public class Elev
{
    public int Id
    {
        get;
        set;
    }

    public string? Nume
    {
        get;
        set;
    }

    public string? Scoala
    {
        get;
        set;
    }

    public override string ToString()
    {
        return $"{Nume} - {Scoala}";
    }
}
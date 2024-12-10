using System;

namespace seminar11;

public class Angajat : Entity<int>
{
    private String _nume;
    private String _prenume;
    private int _salariu;

    public Angajat(int id,string nume, string prenume, int salariu)
    {
        this.ID = id;
        this._nume = nume;
        this._prenume = prenume;
        this._salariu = salariu;
    }

    public string Nume
    {
        get => _nume;
        set => _nume = value;
    }

    public string Prenume
    {
        get => _prenume;
        set => _prenume = value;
    }

    public int Salariu
    {
        get => _salariu;
        set => _salariu = value;
    }

    public override string ToString()
    {
        return $"{ID}: {Nume} {Prenume} - {Salariu}";
    }

    public override bool Equals(object obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;
        Angajat angajat = (Angajat)obj;
        return _salariu == angajat._salariu && _nume.Equals(angajat._nume) && _prenume.Equals(angajat._prenume);
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(_nume, _prenume, _salariu);
    }

    public static bool operator ==(Angajat angajat1, Angajat angajat2)
    {
        if (ReferenceEquals(angajat1, angajat2)) return true;
        if (ReferenceEquals(angajat1, null)) return false;
        if (ReferenceEquals(angajat2, null)) return false;
        return angajat1.Equals(angajat2);
    }

    public static bool operator !=(Angajat angajat1, Angajat angajat2)
    {
        return !(angajat1 == angajat2);
    }
}
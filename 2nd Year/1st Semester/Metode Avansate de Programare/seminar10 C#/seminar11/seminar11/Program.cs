namespace seminar11;

class Mainn
{
    static AngajatValidator _validator = new AngajatValidator();
    InMemoryRepository<int, Angajat> _repo = new InMemoryRepository<int, Angajat>(_validator) ;
    Angajat _a = new Angajat(1,"Popescu", "Ion", 3000);
    Angajat _b = new Angajat(2,"Ionescu", "Paul", 1200);

    public void Run()
    {
        _repo.Save(_a);
        _repo.Save(_b);
        foreach (var angajat in _repo.FindAll())
        {
            Console.WriteLine(angajat);
        }

        // _repo.Save(new Angajat(-1, "Anderi", "Pop", 6000));  ///CRAPA

        _repo.Update(new Angajat(1, "Lauru", "Balauru", 100));

        foreach (var variable in _repo.FindAll())
        {
            Console.WriteLine(variable);
        }


        // Angajat g = _repo.FindOne(30); /// Nu afiseaza nimic
        // Console.WriteLine(g);

    }

    public static void Main(string[] args)
    {
        Mainn m = new Mainn();
        m.Run();
    }
}
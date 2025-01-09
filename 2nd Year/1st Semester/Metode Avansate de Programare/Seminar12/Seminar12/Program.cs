using Sem11_12.Service;
using Seminar12.Model;

namespace Seminar12;

class Mainn
{
    public Mainn()
    {
        InFileRepository<string, Angajat> _inFileRepositoryAngajat = new AngajatInFileRepository(new AngajatValidator(), "angajati.txt");
        InFileRepository<string, Sarcina> _inFileRepositorySarcina = new SarcinaInFileRepository(new SarcinaValidator(), "sarcini.txt");
        AngajatService _angajatService = new AngajatService(_inFileRepositoryAngajat);
        SarcinaService _sarcinaService = new SarcinaService(_inFileRepositorySarcina);
        
    }
}
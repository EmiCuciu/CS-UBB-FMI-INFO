
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seminar12.Model;

namespace Sem11_12.Service
{
    class AngajatService
    {
        private IRepository<string, Angajat> repo;

        public AngajatService(IRepository<string, Angajat> repo)
        {
            this.repo = repo;
        }


        public List<Angajat> FindAllAngajati()
        {
            return repo.FindAll().ToList();
        }
        
        public Angajat AddAngajat(Angajat angajat)
        {
            return repo.Save(angajat);
        }
    }
}



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Seminar12.Model;

namespace Sem11_12.Service
{
    class SarcinaService
    {
        private IRepository<string, Sarcina> repo;

        public SarcinaService(IRepository<string, Sarcina> repo)
        {
            this.repo = repo;
        }



        public List<Sarcina> FindAllSarcini()
        {
            return repo.FindAll().ToList();
        }
    }
}


using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace Seminar12.Model
{
    class AngajatInFileRepository : InFileRepository<string, Angajat>
    {

        public AngajatInFileRepository(IValidator<Angajat> vali, string fileName) : base(vali, fileName, EntityToFileMapping.CreateAngajat)           
        {
           
        }

    }
}

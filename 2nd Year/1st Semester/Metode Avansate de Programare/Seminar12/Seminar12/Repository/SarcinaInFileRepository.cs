using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seminar12.Model{
    class SarcinaInFileRepository : InFileRepository<string, Sarcina>
    {

        public SarcinaInFileRepository(IValidator<Sarcina> vali, string fileName) : base(vali, fileName, EntityToFileMapping.CreateSarcina)
        {
            
        }

    }

}

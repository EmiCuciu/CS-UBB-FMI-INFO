using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seminar12.Model
{
    enum KnowledgeLevel
    {
        Junior=1, Medium, Senior
    }
    class Angajat : Entity<string> 
    {
        public String Nume { get; set; }
        public double VenitPeOra { get; set; }
        public KnowledgeLevel Nivel { get; set; }

       

        public override string ToString()
        {
            return ID+" "+Nume+" "+VenitPeOra+" "+Nivel;
        }
    }
}

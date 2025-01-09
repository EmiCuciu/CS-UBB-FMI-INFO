using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seminar12.Model{
    class ValidationException : ApplicationException
    {
        public ValidationException(String message): base(message)
        {
        }
    }
}

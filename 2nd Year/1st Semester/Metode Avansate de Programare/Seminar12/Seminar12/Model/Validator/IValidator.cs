using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seminar12.Model{
    interface IValidator<E>
    {
        void Validate(E e);
    }
}

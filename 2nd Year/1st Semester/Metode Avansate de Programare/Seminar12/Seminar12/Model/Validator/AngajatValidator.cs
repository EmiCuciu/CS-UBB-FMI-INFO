﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Seminar12.Model
{
    class AngajatValidator : IValidator<Angajat>
    {
        public void Validate(Angajat e)
        {
            bool valid = true;
            if (valid == false)
            {
                throw new ValidationException("Obiectul nu e valid");
            }
        }
    }
}

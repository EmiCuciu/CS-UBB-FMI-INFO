using System.ComponentModel.DataAnnotations;

namespace seminar11;

public class AngajatValidator : IValidator<Angajat>
{
    public void Validate(Angajat entity)
    {
        if (entity.ID < 0)
        {
            throw new ValidationException("ID invalid!");
        }

        if (entity.Nume == "")
        {
            throw new ValidationException("Nume invalid!");
        }
        
        if (entity.Salariu < 0)
        {
            throw new ValidationException("Salariu invalid!");
        }
        
        if(entity.Prenume == "")
        {
            throw new ValidationException("Prenume invalid!");
        }
    }
    
}
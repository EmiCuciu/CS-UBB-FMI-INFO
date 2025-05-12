using System;
using System.Collections.Generic;
using System.Linq;
using Lab1.Domain;
using Lab1.Repository;

namespace Lab1.Service
{
    public class AuthentificationService
    {
        private readonly IArbitruRepository repository;
        private Arbitru currentUser;

        public AuthentificationService(IArbitruRepository repository)
        {
            this.repository = repository;
        }

        public Arbitru Login(string username, string password)
        {
            var arbitri = repository.FindAll();
            foreach (var arbitru in arbitri)
            {
                if (arbitru.Username == username && arbitru.Password == password)
                {
                    currentUser = arbitru;
                    return arbitru;
                }
            }
            return null;
        }

        public bool Register(string username, string password, string firstName, string lastName)
        {
            var arbitri = repository.FindAll();
            if (arbitri.Any(a => a.Username == username))
            {
                return false;
            }

            var newArbitru = new Arbitru(0, username, password, firstName, lastName);
            repository.Save(newArbitru);
            return true;
        }

        public void Logout()
        {
            currentUser = null;
        }
    }
}

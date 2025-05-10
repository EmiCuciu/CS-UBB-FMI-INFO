using System;
using System.Collections.Generic;
using System.Linq;
using Lab1.Domain;
using Lab1.Repository;

namespace Lab1.Service
{
    public class ProbaService
    {
        private readonly ProbaRepository probaRepository;

        public ProbaService(ProbaRepository probaRepository)
        {
            this.probaRepository = probaRepository;
        }

        public List<Proba> GetAllProbe()
        {
            return probaRepository.FindAll().ToList();
        }

        public TipProba? GetProbaForArbitru(Arbitru arbitru)
        {
            foreach (var proba in GetAllProbe())
            {
                if (proba.Arbitru.Id == arbitru.Id)
                {
                    return proba.TipProba;
                }
            }
            return null;
        }

        public Proba GetProbaByArbitru(Arbitru arbitru)
        {
            foreach (var proba in GetAllProbe())
            {
                if (proba.Arbitru.Id == arbitru.Id)
                {
                    return proba;
                }
            }
            return null;
        }
    }
}

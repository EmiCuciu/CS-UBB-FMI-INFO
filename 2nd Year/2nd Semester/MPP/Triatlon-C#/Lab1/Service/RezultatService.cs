using System;
using System.Collections.Generic;
using System.Linq;
using Lab1.Domain;
using Lab1.Repository;
using log4net;

namespace Lab1.Service
{
    public class RezultatService
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(RezultatService));
        private readonly IRezultatRepository rezultatRepository;

        public event EventHandler RezultatAdded;

        public RezultatService(IRezultatRepository rezultatRepository)
        {
            this.rezultatRepository = rezultatRepository;
        }

        public void AddRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj)
        {
            logger.Info($"Adding result for participant {participant.Id}: {punctaj} points in {tipProba}");

            var rezultat = new Rezultat(0, participant, arbitru, tipProba, punctaj);
            rezultatRepository.Save(rezultat);

            participant.SetPunctajProba(tipProba, punctaj);

            RezultatAdded?.Invoke(this, EventArgs.Empty);
        }

        public List<Rezultat> GetRezultateForProba(TipProba tipProba)
        {
            var rezultate = rezultatRepository.FindAll()
                .Where(r => r.TipProba == tipProba)
                .OrderByDescending(r => r.Points)
                .ToList();
            return rezultate;
        }
    }
}


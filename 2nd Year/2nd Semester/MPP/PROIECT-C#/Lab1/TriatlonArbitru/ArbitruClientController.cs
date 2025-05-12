using System;
using TriatlonModel;
using TriatlonServicess;

namespace TriatlonArbitru
{
    public class ArbitruClientController : ITriatlonObserver
    {
        // Event handlers for observer callbacks
        public event Action<Arbitru> OnArbitruLoggedIn;
        public event Action<Arbitru> OnArbitruLoggedOut;
        public event Action<Rezultat> OnRezultatAdded;

        public void ArbitruLoggedIn(Arbitru arbitru)
        {
            OnArbitruLoggedIn?.Invoke(arbitru);
        }

        public void ArbitruLoggedOut(Arbitru arbitru)
        {
            OnArbitruLoggedOut?.Invoke(arbitru);
        }

        public void RezultatAdded(Rezultat rezultat)
        {
            OnRezultatAdded?.Invoke(rezultat);
        }
    }
}
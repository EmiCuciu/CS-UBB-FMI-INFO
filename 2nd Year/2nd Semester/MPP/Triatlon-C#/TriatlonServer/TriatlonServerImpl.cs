using System.Collections.Concurrent;
using log4net;
using TriatlonModel;
using TriatlonPersistance;
using TriatlonServicess;

namespace TriatlonServer
{
    public class TriatlonServicesImpl : ITriatlonServices
    {
        private readonly IArbitruRepository _arbitruRepository;
        private readonly IRezultatRepository _rezultatRepository;
        private readonly IParticipantRepository _participantRepository;
        private readonly IProbaRepository _probaRepository;
        private static readonly ILog Logger = LogManager.GetLogger(typeof(TriatlonServicesImpl));

        private readonly ConcurrentDictionary<string, ITriatlonObserver> _loggedClients;

        public TriatlonServicesImpl(IArbitruRepository arbitruRepo, IRezultatRepository rezultatRepo,
            IParticipantRepository participantRepo, IProbaRepository probaRepo)
        {
            _arbitruRepository = arbitruRepo;
            _rezultatRepository = rezultatRepo;
            _participantRepository = participantRepo;
            _probaRepository = probaRepo;
            _loggedClients = new ConcurrentDictionary<string, ITriatlonObserver>();
        }

        // Authentication
        public void Login(Arbitru arbitru, ITriatlonObserver client)
        {
            lock (this)
            {
                var arbitri = _arbitruRepository.FindAll();
                foreach (var a in arbitri)
                {
                    if (a.Username == arbitru.Username && a.Password == arbitru.Password)
                    {
                        _loggedClients[arbitru.Username] = client;
                        Logger.Info($"Arbitru {arbitru.Username} logged in");
                        NotifyArbitriiLoggedIn(arbitru);
                        return;
                    }
                }

                throw new TriatlonException("Invalid username or password");
            }
        }

        private void NotifyArbitriiLoggedIn(Arbitru arbitru)
        {
            Logger.Debug($"Notifying all arbitrii about the logged in arbitru: {arbitru.Username}");
            var tasks = new List<Task>();
            foreach (var client in _loggedClients.Values)
            {
                tasks.Add(Task.Run(() =>
                {
                    try
                    {
                        client.ArbitruLoggedIn(arbitru);
                    }
                    catch (Exception e)
                    {
                        Logger.Error($"Error notifying arbitru: {e.Message}");
                    }
                }));
            }

            Task.WaitAll(tasks.ToArray());
        }

        public void Logout(Arbitru arbitru, ITriatlonObserver client)
        {
            lock (this)
            {
                if (_loggedClients.TryRemove(arbitru.Username, out _))
                {
                    Logger.Info($"Arbitru {arbitru.Username} logged out");
                    NotifyArbitriiLoggedOut(arbitru);
                }
                else
                {
                    throw new TriatlonException($"Arbitru {arbitru.Username} is not logged in");
                }
            }
        }

        private void NotifyArbitriiLoggedOut(Arbitru arbitru)
        {
            Logger.Debug($"Notifying all arbitrii about the logged out arbitru: {arbitru.Username}");
            var tasks = new List<Task>();
            foreach (var client in _loggedClients.Values)
            {
                tasks.Add(Task.Run(() =>
                {
                    try
                    {
                        client.ArbitruLoggedOut(arbitru);
                    }
                    catch (Exception e)
                    {
                        Logger.Error($"Error notifying arbitru: {e.Message}");
                    }
                }));
            }

            Task.WaitAll(tasks.ToArray());
        }

        public bool Register(string username, string password, string firstName, string lastName)
        {
            lock (this)
            {
                var arbitri = _arbitruRepository.FindAll();
                if (arbitri.Any(a => a.Username == username))
                {
                    Logger.Warn($"Username {username} already exists");
                    return false;
                }

                var newArbitru = new Arbitru(0, username, password, firstName, lastName);
                _arbitruRepository.Save(newArbitru);
                Logger.Info($"Arbitru {username} registered successfully");
                return true;
            }
        }

        // Rezultat Service
        public void AddRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj)
        {
            lock (this)
            {
                Logger.Info($"Adding rezultat for participant {participant.Id}: {punctaj} points in {tipProba}");
                var rezultat = new Rezultat(0, participant, arbitru, tipProba, punctaj);
                _rezultatRepository.Save(rezultat);

                participant.SetPunctajProba(tipProba, punctaj);

                Logger.Info($"Rezultat added: {rezultat}");
                NotifyRezultatAdded(rezultat);
            }
        }

        private void NotifyRezultatAdded(Rezultat rezultat)
        {
            Logger.Debug($"Notifying all arbitrii about the added rezultat: {rezultat}");
            var tasks = new List<Task>();
            foreach (var client in _loggedClients.Values)
            {
                tasks.Add(Task.Run(() =>
                {
                    try
                    {
                        client.RezultatAdded(rezultat);
                    }
                    catch (Exception e)
                    {
                        Logger.Error($"Error notifying arbitru: {e.Message}");
                    }
                }));
            }

            Task.WaitAll(tasks.ToArray());
        }

        public List<Rezultat> GetResultateForProba(TipProba tipProba)
        {
            lock (this)
            {
                return _rezultatRepository.FindAll()
                    .Where(r => r.TipProba == tipProba)
                    .OrderByDescending(r => r.Points)
                    .ToList();
            }
        }

        public List<Rezultat> GetAllResults()
        {
            lock (this)
            {
                return _rezultatRepository.FindAll().ToList();
            }
        }

        // Participant Service
        public List<Participant> GetAllParticipants()
        {
            lock (this)
            {
                var participants = _participantRepository.FindAll().ToList();
                participants.Sort((p1, p2) => string.Compare(p1.LastName, p2.LastName, StringComparison.Ordinal));
                return participants;
            }
        }

        public int CalculateTotalScore(Participant participant)
        {
            lock (this)
            {
                if (participant == null) return 0;

                var total = 0;
                foreach (var rezultat in _rezultatRepository.FindAll())
                {
                    if (rezultat.Participant.Id == participant.Id)
                    {
                        participant.SetPunctajProba(rezultat.TipProba, rezultat.Points);
                        total += rezultat.Points;
                    }
                }

                return total;
            }
        }

        // Proba Service
        public List<Proba> GetAllProbe()
        {
            lock (this)
            {
                return _probaRepository.FindAll().ToList();
            }
        }

        public TipProba GetProbaForArbitru(Arbitru arbitru)
        {
            lock (this)
            {
                return _probaRepository.FindAll()
                           .FirstOrDefault(p => p.Arbitru.Id == arbitru.Id)?.TipProba
                       ?? throw new InvalidOperationException("No matching Proba found for the given Arbitru.");
            }
        }

        public Arbitru FindArbitruByUsernameAndPassword(string username, string password)
        {
            return _arbitruRepository.FindBy(username, password);
        }
    }
}
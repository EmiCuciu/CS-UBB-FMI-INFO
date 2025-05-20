using System.Collections.Concurrent;
using System.Net.Sockets;
using System.Text.Json;
using TriatlonModel;
using TriatlonNetworking.dto;
using TriatlonServicess;

namespace TriatlonNetworking.jsonprotocol
{
    public class TriatlonServerJsonProxy : ITriatlonServices
    {
        private readonly string host;
        private readonly int port;

        private ITriatlonObserver client;
        private StreamReader input;
        private StreamWriter output;
        private TcpClient connection;

        private readonly BlockingCollection<Response> responses;
        private volatile bool finished;
        private readonly JsonSerializerOptions jsonOptions;

        public TriatlonServerJsonProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new BlockingCollection<Response>();
            jsonOptions = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
        }

        private void InitializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                input = new StreamReader(connection.GetStream());
                output = new StreamWriter(connection.GetStream()) { AutoFlush = true };
                finished = false;
                StartReader();
            }
            catch (Exception e)
            {
                throw new TriatlonException("Error connecting to server: " + e.Message);
            }
        }

        private void CloseConnection()
        {
            finished = true;
            try
            {
                input?.Close();
                output?.Close();
                connection?.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error closing connection: " + e.Message);
            }
        }

        private void StartReader()
        {
            new Thread(() =>
            {
                while (!finished)
                {
                    try
                    {
                        var responseLine = input.ReadLine();
                        if (responseLine == null)
                        {
                            finished = true;
                            break;
                        }

                        var response = JsonSerializer.Deserialize<Response>(responseLine, jsonOptions);
                        if (IsUpdate(response))
                        {
                            HandleUpdate(response);
                        }
                        else
                        {
                            responses.Add(response);
                        }
                    }
                    catch (Exception e)
                    {
                        finished = true;
                    }
                }
            }).Start();
        }

        private void SendRequest(Request request)
        {
            try
            {
                var requestStr = JsonSerializer.Serialize(request, jsonOptions);
                output.WriteLine(requestStr);
            }
            catch (Exception e)
            {
                throw new TriatlonException("Error sending request: " + e.Message);
            }
        }

        private Response ReadResponse()
        {
            try
            {
                return responses.Take();
            }
            catch (Exception e)
            {
                throw new TriatlonException("Error reading response: " + e.Message);
            }
        }

        private bool IsUpdate(Response response)
        {
            return response.Type == ResponseType.REFREE_LOGGED_IN ||
                   response.Type == ResponseType.REFREE_LOGGED_OUT ||
                   response.Type == ResponseType.REZULTAT_ADDED;
        }

        private void HandleUpdate(Response response)
        {
            if (client == null) return;

            switch (response.Type)
            {
                case ResponseType.REFREE_LOGGED_IN:
                    var arbitruLoggedIn = DTOUtils.GetFromDTO(response.ArbitruDTO);
                    client.ArbitruLoggedIn(arbitruLoggedIn);
                    break;
                case ResponseType.REFREE_LOGGED_OUT:
                    var arbitruLoggedOut = DTOUtils.GetFromDTO(response.ArbitruDTO);
                    client.ArbitruLoggedOut(arbitruLoggedOut);
                    break;
                case ResponseType.REZULTAT_ADDED:
                    var rezultatAdded = DTOUtils.GetFromDTO(response.RezultatDTO);
                    client.RezultatAdded(rezultatAdded);
                    break;
            }
        }

        public void Login(Arbitru arbitru, ITriatlonObserver iTriatlonObserver)
        {
            InitializeConnection();
            client = iTriatlonObserver;

            var req = JsonProtocolUtils.CreateLoginRequest(arbitru);
            SendRequest(req);
            var response = ReadResponse();

            if (response.Type != ResponseType.OK)
            {
                CloseConnection();
                throw new TriatlonException("Login failed");
            }
        }

        public bool Register(string username, string password, string firstName, string lastName)
        {
            if (connection == null)
            {
                InitializeConnection();
            }

            var arbitru = new Arbitru(0, username, password, firstName, lastName);
            var req = new Request
            {
                Type = RequestType.REGISTER,
                ArbitruDTO = DTOUtils.GetDTO(arbitru)
            };
            SendRequest(req);
            var response = ReadResponse();
            return response.Type == ResponseType.OK;
        }

        public void Logout(Arbitru arbitru, ITriatlonObserver iTriatlonObserver)
        {
            var req = JsonProtocolUtils.CreateLogoutRequest(arbitru);
            SendRequest(req);
            ReadResponse();
            CloseConnection();
            client = null;
        }

        public void AddRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj)
        {
            var rezultat = new Rezultat(0, participant, arbitru, tipProba, punctaj);
            var req = JsonProtocolUtils.CreateAddResultRequest(arbitru, rezultat);
            SendRequest(req);
            var response = ReadResponse();

            if (response.Type != ResponseType.OK)
            {
                throw new TriatlonException("Failed to add result");
            }
        }

        public List<Rezultat> GetResultateForProba(TipProba proba)
        {
            var req = new Request
            {
                Type = RequestType.GET_RESULTS_FOR_PROBA,
                TipProbaDTO = DTOUtils.GetTipProbaDTO(proba)
            };
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.GET_RESULTS_FOR_PROBA
                ? response.RezultateDTO.Select(DTOUtils.GetFromDTO).ToList()
                : new List<Rezultat>();
        }

        public List<Rezultat> GetAllResults()
        {
            var req = new Request { Type = RequestType.GET_ALL_RESULTS };
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.GET_ALL_RESULTS
                ? response.RezultateDTO.Select(DTOUtils.GetFromDTO).ToList()
                : new List<Rezultat>();
        }

        public List<Participant> GetAllParticipants()
        {
            var req = new Request { Type = RequestType.GET_ALL_PARTICIPANTS };
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.GET_ALL_PARTICIPANTS
                ? response.ParticipantiDTO.Select(DTOUtils.GetFromDTO).ToList()
                : new List<Participant>();
        }

        public int CalculateTotalScore(Participant participant)
        {
            var req = new Request
            {
                Type = RequestType.CALCULATE_TOTAL_SCORE,
                ParticipantDTO = DTOUtils.GetDTO(participant)
            };
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.OK
                ? int.Parse(response.ErrorMessage)
                : 0;
        }

        public List<Proba> GetAllProbe()
        {
            var req = new Request { Type = RequestType.GET_ALL_PROBE };
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.OK
                ? response.ProbeDTO.Select(dto =>
                        new Proba(dto.Id ?? 0, DTOUtils.GetFromDTO(dto.TipProba), DTOUtils.GetFromDTO(dto.Arbitru)))
                    .ToList()
                : new List<Proba>();
        }

        public TipProba GetProbaForArbitru(Arbitru arbitru)
        {
            var req = new Request
            {
                Type = RequestType.GET_PROBA_FOR_ARBITRU,
                ArbitruDTO = DTOUtils.GetDTO(arbitru)
            };
            SendRequest(req);
            Response response = ReadResponse();

            return response.Type == ResponseType.OK
                ? DTOUtils.GetFromDTO(response.GetTipProba())
                : throw new TriatlonException("No proba found for arbitru");
        }

        public Arbitru FindArbitruByUsernameAndPassword(string username, string password)
        {
            InitializeConnection();

            var req = JsonProtocolUtils.CreateFindArbitruRequest(username, password);
            SendRequest(req);
            var response = ReadResponse();

            return response.Type == ResponseType.OK
                ? DTOUtils.GetFromDTO(response.ArbitruDTO)
                : null;
        }
    }
}
using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading;
using log4net;
using TriatlonModel;
using TriatlonNetworking.dto;
using TriatlonServicess;

namespace TriatlonNetworking.jsonprotocol
{
    public class TriatlonClientJsonWorker : ITriatlonObserver
    {
        private readonly ITriatlonServices _server;
        private readonly TcpClient _connection;
        private StreamReader _input;
        private StreamWriter _output;
        private readonly JsonSerializerOptions _jsonOptions;
        private volatile bool _connected;
        private static readonly ILog Logger = LogManager.GetLogger(typeof(TriatlonClientJsonWorker));

        public TriatlonClientJsonWorker(ITriatlonServices server, TcpClient connection)
        {
            _server = server;
            _connection = connection;
            _jsonOptions = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
            try
            {
                _output = new StreamWriter(_connection.GetStream()) { AutoFlush = true };
                _input = new StreamReader(_connection.GetStream());
                _connected = true;
            }
            catch (IOException e)
            {
                Logger.Error("Error creating worker: " + e.Message);
            }
        }

        public void Run()
        {
            while (_connected)
            {
                try
                {
                    string requestLine = _input.ReadLine();
                    if (requestLine == null) break;

                    var request = JsonSerializer.Deserialize<Request>(requestLine, _jsonOptions);
                    var response = HandleRequest(request);
                    if (response != null)
                    {
                        SendResponse(response);
                    }
                }
                catch (IOException e)
                {
                    Logger.Error(e);
                }
                Thread.Sleep(1000);
            }

            try
            {
                _input.Close();
                _output.Close();
                _connection.Close();
            }
            catch (IOException e)
            {
                Logger.Error("Error closing connection: " + e.Message);
            }
        }

        private void SendResponse(Response response)
        {
            string responseLine = JsonSerializer.Serialize(response, _jsonOptions);
            Logger.Debug("Sending response: " + responseLine);
            lock (_output)
            {
                _output.WriteLine(responseLine);
            }
        }

        // ITriatlonObserver methods
        public void ArbitruLoggedIn(Arbitru arbitru)
        {
            var response = JsonProtocolUtils.CreateArbitruLoggedInResponse(arbitru);
            SendResponse(response);
        }

        public void ArbitruLoggedOut(Arbitru arbitru)
        {
            var response = JsonProtocolUtils.CreateArbitruLoggedOutResponse(arbitru);
            SendResponse(response);
        }

        public void RezultatAdded(Rezultat rezultat)
        {
            var response = JsonProtocolUtils.CreateRezultatAddedResponse(rezultat);
            SendResponse(response);
        }

        private static readonly Response OkResponse = JsonProtocolUtils.CreateOkResponse();

        private Response HandleRequest(Request request)
        {
            try
            {
                switch (request.Type)
                {
                    case RequestType.LOGIN:
                        Logger.Debug($"Login request: {request.ArbitruDTO}");
                        var arbitru = DTOUtils.GetFromDTO(request.ArbitruDTO);
                        _server.Login(arbitru, this);
                        return OkResponse;

                    case RequestType.LOGOUT:
                        Logger.Debug($"Logout request: {request.ArbitruDTO}");
                        arbitru = DTOUtils.GetFromDTO(request.ArbitruDTO);
                        _server.Logout(arbitru, this);
                        _connected = false;
                        return OkResponse;

                    case RequestType.ADD_RESULT:
                        Logger.Debug($"Add result request: {request.RezultatDTO}");
                        var rezultat = DTOUtils.GetFromDTO(request.RezultatDTO);
                        _server.AddRezultat(rezultat.Participant, rezultat.Arbitru, rezultat.TipProba, rezultat.Points);
                        return OkResponse;

                    case RequestType.GET_RESULTS_FOR_PROBA:
                        Logger.Debug($"Get results for proba request: {request.TipProbaDTO}");
                        var tipProba = DTOUtils.GetFromDTO(request.TipProbaDTO);
                        var rezultate = _server.GetResultateForProba(tipProba);
                        var rezultatDTOs = DTOUtils.GetDTO(rezultate.ToArray());
                        return JsonProtocolUtils.CreateGetResultsForProbaResponse(rezultatDTOs);

                    case RequestType.GET_PROBA_FOR_ARBITRU:
                        Logger.Debug($"Get proba for arbitru request: {request.ArbitruDTO}");
                        arbitru = DTOUtils.GetFromDTO(request.ArbitruDTO);
                        var proba = _server.GetProbaForArbitru(arbitru);
                        var probaDTO = proba != null
                            ? new ProbaDTO(null, DTOUtils.GetTipProbaDTO(proba), request.ArbitruDTO)
                            : null;
                        return new Response { Type = ResponseType.OK, ProbaDTO = probaDTO };

                    case RequestType.GET_ALL_PARTICIPANTS:
                        Logger.Debug("Get all participants request");
                        var participants = _server.GetAllParticipants();
                        var participantDTOs = participants.ConvertAll(DTOUtils.GetDTO).ToArray();
                        return new Response { Type = ResponseType.GET_ALL_PARTICIPANTS, ParticipantiDTO = participantDTOs };

                    case RequestType.CALCULATE_TOTAL_SCORE:
                        Logger.Debug("Calculate total score request");
                        var participant = DTOUtils.GetFromDTO(request.ParticipantDTO);
                        var totalScore = _server.CalculateTotalScore(participant);
                        return new Response { Type = ResponseType.OK, ErrorMessage = totalScore.ToString() };

                    case RequestType.GET_ALL_PROBE:
                        Logger.Debug("Get all probe request");
                        var probe = _server.GetAllProbe();
                        var probeDTOs = probe.ConvertAll(p => DTOUtils.GetDTO(p.Id, p.TipProba, p.Arbitru)).ToArray();
                        return new Response { Type = ResponseType.OK, ProbeDTO = probeDTOs };

                    case RequestType.FIND_ARBITRU:
                        Logger.Debug($"Find arbitru request: {request.ArbitruDTO}");
                        arbitru = _server.FindArbitruByUsernameAndPassword(request.ArbitruDTO.Username, request.ArbitruDTO.Password);
                        return arbitru != null
                            ? new Response { Type = ResponseType.OK, ArbitruDTO = DTOUtils.GetDTO(arbitru) }
                            : JsonProtocolUtils.CreateErrorResponse("Invalid credentials");

                    default:
                        return JsonProtocolUtils.CreateErrorResponse("Unknown request type");
                }
            }
            catch (TriatlonException e)
            {
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
    }
}
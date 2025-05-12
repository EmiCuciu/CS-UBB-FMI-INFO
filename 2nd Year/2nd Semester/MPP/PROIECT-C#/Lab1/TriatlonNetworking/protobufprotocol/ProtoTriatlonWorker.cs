using System.Net.Sockets;
using Google.Protobuf;
using log4net;
using Triatlon.Network.ProtobufProtocol;
using TriatlonServicess;
using Arbitru = TriatlonModel.Arbitru;
using Participant = TriatlonModel.Participant;
using Rezultat = TriatlonModel.Rezultat;
using TipProba = TriatlonModel.TipProba;

namespace TriatlonNetworking.protobufprotocol
{
    public class ProtoTriatlonWorker : ITriatlonObserver
    {
        private readonly ITriatlonServices _server;
        private readonly TcpClient _connection;

        private NetworkStream _stream;
        private volatile bool _connected;

        private static readonly ILog Logger = LogManager.GetLogger(typeof(ProtoTriatlonWorker));

        public ProtoTriatlonWorker(ITriatlonServices server, TcpClient connection)
        {
            _server = server;
            _connection = connection;
            try
            {
                _stream = connection.GetStream();
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
                    // Read the request
                    Request request = Request.Parser.ParseDelimitedFrom(_stream);
                    if (request == null)
                    {
                        break;
                    }

                    Logger.Debug($"Received request: {request}");

                    // Process request and get response
                    Response response = HandleRequest(request);
                    if (response != null)
                    {
                        SendResponse(response);
                    }
                }
                catch (IOException e)
                {
                    Logger.Error($"Error processing request: {e.Message}");
                    _connected = false;
                }

                Thread.Sleep(10);
            }

            try
            {
                _stream.Close();
                _connection.Close();
            }
            catch (IOException e)
            {
                Logger.Error($"Error closing connection: {e.Message}");
            }
        }

        private void SendResponse(Response response)
        {
            Logger.Debug($"Sending response: {response}");
            lock (_stream)
            {
                response.WriteDelimitedTo(_stream);
                _stream.Flush();
            }
        }

        // ITriatlonObserver methods
        public void ArbitruLoggedIn(Arbitru arbitru)
        {
            Response response = ProtoUtils.CreateArbitruLoggedInResponse(arbitru);
            try
            {
                SendResponse(response);
            }
            catch (IOException e)
            {
                throw new TriatlonException("Error sending arbitru logged in notification: " + e.Message);
            }
        }

        public void ArbitruLoggedOut(Arbitru arbitru)
        {
            Response response = ProtoUtils.CreateArbitruLoggedOutResponse(arbitru);
            try
            {
                SendResponse(response);
            }
            catch (IOException e)
            {
                throw new TriatlonException("Error sending arbitru logged out notification: " + e.Message);
            }
        }

        public void RezultatAdded(Rezultat rezultat)
        {
            Response response = ProtoUtils.CreateAddedResultResponse(rezultat);
            try
            {
                SendResponse(response);
            }
            catch (IOException e)
            {
                throw new TriatlonException("Error sending rezultat added notification: " + e.Message);
            }
        }

        private static readonly Response OkResponse = ProtoUtils.CreateOkResponse();

        private Response HandleRequest(Request request)
        {
            switch (request.Type)
            {
                case Request.Types.Type.Login:
                    Logger.Debug($"Login request: {request.Arbitru}");
                    Arbitru loginArbitru = ProtoUtils.FromProto(request.Arbitru);
                    try
                    {
                        _server.Login(loginArbitru, this);
                        return OkResponse;
                    }
                    catch (TriatlonException e)
                    {
                        _connected = false;
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.Logout:
                    Logger.Debug($"Logout request: {request.Arbitru}");
                    Arbitru logoutArbitru = ProtoUtils.FromProto(request.Arbitru);
                    try
                    {
                        _server.Logout(logoutArbitru, this);
                        _connected = false;
                        return OkResponse;
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.Register:
                    Logger.Debug($"Register request: {request.Arbitru}");
                    Arbitru registerArbitru = ProtoUtils.FromProto(request.Arbitru);
                    try
                    {
                        bool success = _server.Register(
                            registerArbitru.Username,
                            registerArbitru.Password,
                            registerArbitru.FirstName,
                            registerArbitru.LastName
                        );
                        return success
                            ? OkResponse
                            : ProtoUtils.CreateErrorResponse("Registration failed - username already exists");
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.AddResult:
                    Logger.Debug("Add result request");
                    try
                    {
                        Participant participant = ProtoUtils.FromProto(request.Participant);
                        Arbitru arbitru = ProtoUtils.FromProto(request.Arbitru);

                        TipProba tipProba = _server.GetProbaForArbitru(arbitru);
                        int punctaj = request.Punctaj;

                        Logger.Debug(
                            $"participant: {participant}, arbitru: {arbitru}, tipProba: {tipProba}, punctaj: {punctaj}");

                        if (tipProba == null)
                        {
                            return ProtoUtils.CreateErrorResponse("TipProba is null");
                        }

                        _server.AddRezultat(participant, arbitru, tipProba, punctaj);
                        return OkResponse;
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.GetResultsForProba:
                    Logger.Debug("Get results for proba request");
                    try
                    {
                        TipProba tipProba = ProtoUtils.FromProto(request.TipProba);
                        var rezultate = _server.GetResultateForProba(tipProba);
                        return ProtoUtils.CreateGetResultsForProbaResponse(rezultate);
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.GetAllParticipants:
                    Logger.Debug("Get all participants request");
                    try
                    {
                        List<Participant> participants = _server.GetAllParticipants();
                        return ProtoUtils.CreateGetAllParticipantsResponse(participants);
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.CalculateTotalScore:
                    Logger.Debug("Calculate total score request");
                    try
                    {
                        Participant participant = ProtoUtils.FromProto(request.Participant);
                        int totalScore = _server.CalculateTotalScore(participant);
                        return ProtoUtils.CreateCalculateTotalScoreResponse(totalScore);
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.GetAllProbe:
                    Logger.Debug("Get all probe request");
                    try
                    {
                        var probe = _server.GetAllProbe();
                        return ProtoUtils.CreateGetAllProbeResponse(probe);
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.GetProbaForArbitru:
                    Logger.Debug("Get proba for arbitru request");
                    try
                    {
                        Arbitru arbitru = ProtoUtils.FromProto(request.Arbitru);
                        TipProba tipProba = _server.GetProbaForArbitru(arbitru);
                        return ProtoUtils.CreateGetProbaForArbitruResponse(tipProba);
                    }
                    catch (TriatlonException e)
                    {
                        return ProtoUtils.CreateErrorResponse(e.Message);
                    }

                case Request.Types.Type.FindArbitru:
                    Logger.Debug("Find arbitru request");
                    var arbitruDTO = request.Arbitru;
                    Logger.Debug($"Username: {arbitruDTO.Username}, Password: {arbitruDTO.Password}");

                    Arbitru foundArbitru = _server.FindArbitruByUsernameAndPassword(
                        arbitruDTO.Username,
                        arbitruDTO.Password);

                    if (foundArbitru != null)
                    {
                        return ProtoUtils.CreateFindArbitruResponse(foundArbitru);
                    }
                    else
                    {
                        return ProtoUtils.CreateErrorResponse("Invalid credentials");
                    }


                case Request.Types.Type.GetAllResults:
                    Logger.Debug("Get all results request");
                    var allResults = _server.GetAllResults();
                    return ProtoUtils.CreateGetAllResultsResponse(allResults);

                default:
                    return ProtoUtils.CreateErrorResponse("Invalid request type");
            }
        }
    }
}
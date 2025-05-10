using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Concurrent;
using Google.Protobuf;
using log4net;
using Triatlon.Network.ProtobufProtocol;
using TriatlonNetworking.protobufprotocol;
using TriatlonServicess;

namespace Triatlon.Network.ProtobufProtocol
{
    public class ProtoTriatlonProxy : ITriatlonServices
    {
        private readonly string host;
        private readonly int port;

        private ITriatlonObserver client;
        private TcpClient connection;
        private NetworkStream stream;
        private volatile bool finished;
        private readonly BlockingCollection<Response> responses;
        private static readonly ILog logger = LogManager.GetLogger(typeof(ProtoTriatlonProxy));

        public ProtoTriatlonProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new BlockingCollection<Response>();
        }

        private void InitializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                finished = false;
                StartReader();
            }
            catch (Exception e)
            {
                logger.Error($"Error connecting to server: {e.Message}");
                throw new TriatlonException("Error connecting to server: " + e.Message);
            }
        }

        private void CloseConnection()
        {
            finished = true;
            try
            {
                stream?.Close();
                connection?.Close();
                client = null;
            }
            catch (Exception e)
            {
                logger.Error($"Error closing connection: {e.Message}");
            }
        }

        private void StartReader()
        {
            Thread tw = new Thread(ReaderThread);
            tw.Start();
        }

        private void SendRequest(Request request)
        {
            try
            {
                logger.Debug($"Sending request: {request}");
                request.WriteDelimitedTo(stream);
                stream.Flush();
            }
            catch (Exception e)
            {
                logger.Error($"Error sending request: {e.Message}");
                throw new TriatlonException("Error sending request: " + e.Message);
            }
        }

        private Response ReadResponse()
        {
            try
            {
                Response response = responses.Take();
                logger.Debug($"Response received: {response}");
                if (response.Type == Response.Types.Type.Error)
                {
                    string errorMsg = response.Error;
                    logger.Error($"Error received from server: {errorMsg}");
                    throw new TriatlonException(errorMsg);
                }
                return response;
            }
            catch (Exception e)
            {
                logger.Error($"Error reading response: {e.Message}");
                throw new TriatlonException("Error reading response: " + e.Message);
            }
        }

        private bool IsUpdate(Response response)
        {
            return response.Type == Response.Types.Type.RefreeLoggedIn ||
                   response.Type == Response.Types.Type.RefreeLoggedOut ||
                   response.Type == Response.Types.Type.ResultatAdded;
        }

        private void HandleUpdate(Response response)
        {
            if (client == null) return;
            try
            {
                switch (response.Type)
                {
                    case Response.Types.Type.RefreeLoggedIn:
                        var arbitruLoggedIn = ProtoUtils.FromProto(response.Arbitru);
                        client.ArbitruLoggedIn(arbitruLoggedIn);
                        break;
                    case Response.Types.Type.RefreeLoggedOut:
                        var arbitruLoggedOut = ProtoUtils.FromProto(response.Arbitru);
                        client.ArbitruLoggedOut(arbitruLoggedOut);
                        break;
                    case Response.Types.Type.ResultatAdded:
                        var rezultatAdded = ProtoUtils.FromProto(response.Rezultat);
                        client.RezultatAdded(rezultatAdded);
                        break;
                }
            }
            catch (Exception e)
            {
                logger.Error($"Error handling update: {e.Message}");
            }
        }

        private void ReaderThread()
        {
            while (!finished)
            {
                try
                {
                    Response response = Response.Parser.ParseDelimitedFrom(stream);
                    if (response == null)
                    {
                        logger.Error("Server closed connection");
                        finished = true;
                        break;
                    }
                    logger.Debug($"Response received: {response}");
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
                    logger.Error($"Error reading response: {e.Message}");
                    finished = true;
                }
            }
        }

        // ITriatlonServices implementation

        public void Login(TriatlonModel.Arbitru arbitru, ITriatlonObserver observer)
        {
            InitializeConnection();
            client = observer;

            Request request = ProtoUtils.CreateLoginRequest(arbitru);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type != Response.Types.Type.Ok)
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

            TriatlonModel.Arbitru arbitru = new TriatlonModel.Arbitru(0, username, password, firstName, lastName);
            Request request = ProtoUtils.CreateRegisterRequest(arbitru);
            SendRequest(request);
            Response response = ReadResponse();

            return response.Type == Response.Types.Type.Ok;
        }

        public void Logout(TriatlonModel.Arbitru arbitru, ITriatlonObserver iTriatlonObserver)
        {
            Request request = ProtoUtils.CreateLogoutRequest(arbitru);
            SendRequest(request);
            Response response = ReadResponse();
            CloseConnection();
        }

        public void AddRezultat(TriatlonModel.Participant participant, TriatlonModel.Arbitru arbitru, TriatlonModel.TipProba tipProba, int punctaj)
        {
            Request request = ProtoUtils.CreateAddResultRequest(participant, arbitru, tipProba, punctaj);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type != Response.Types.Type.Ok)
            {
                throw new TriatlonException("Failed to add result");
            }
        }

        public List<TriatlonModel.Rezultat> GetResultateForProba(TriatlonModel.TipProba proba)
        {
            Request request = ProtoUtils.CreateGetResultsForProbaRequest(proba);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type == Response.Types.Type.GetResultsForProba)
            {
                List<TriatlonModel.Rezultat> rezultate = new List<TriatlonModel.Rezultat>();
                foreach (var rezultatProto in response.Rezultate)
                {
                    rezultate.Add(ProtoUtils.FromProto(rezultatProto));
                }
                return rezultate;
            }
            return new List<TriatlonModel.Rezultat>();
        }

        List<TriatlonModel.Rezultat> ITriatlonServices.GetAllResults()
        {
            try
            {
                Request request = ProtoUtils.CreateEmptyRequest(Request.Types.Type.GetAllResults);
                SendRequest(request);
                Response response = ReadResponse();

                if (response.Type == Response.Types.Type.GetAllResults)
                {
                    List<TriatlonModel.Rezultat> rezultate = new List<TriatlonModel.Rezultat>();
                    foreach (var rezultatProto in response.Rezultate)
                    {
                        rezultate.Add(ProtoUtils.FromProto(rezultatProto));
                    }
                    return rezultate;
                }
            }
            catch (Exception e)
            {
                logger.Error($"Error getting all results: {e.Message}");
            }
            return new List<TriatlonModel.Rezultat>();
        }

        List<TriatlonModel.Participant> ITriatlonServices.GetAllParticipants()
        {
            Request request = ProtoUtils.CreateEmptyRequest(Request.Types.Type.GetAllParticipants);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type == Response.Types.Type.GetAllParticipants)
            {
                List<TriatlonModel.Participant> participants = new List<TriatlonModel.Participant>();
                foreach (var participantProto in response.Participants)
                {
                    participants.Add(ProtoUtils.FromProto(participantProto));
                }
                return participants;
            }
            return new List<TriatlonModel.Participant>();
        }

        public int CalculateTotalScore(TriatlonModel.Participant participant)
        {
            Request request = ProtoUtils.CreateCalculateTotalScoreRequest(participant);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type == Response.Types.Type.Ok)
            {
                return response.TotalScore;
            }
            return 0;
        }

        List<TriatlonModel.Proba> ITriatlonServices.GetAllProbe()
        {
            Request request = ProtoUtils.CreateEmptyRequest(Request.Types.Type.GetAllProbe);
            SendRequest(request);
            Response response = ReadResponse();

            if (response.Type == Response.Types.Type.Ok ||
                response.Type == Response.Types.Type.GetAllProbe)
            {
                List<TriatlonModel.Proba> probe = new List<TriatlonModel.Proba>();
                foreach (var probaProto in response.Probe)
                {
                    probe.Add(ProtoUtils.FromProto(probaProto));
                }
                return probe;
            }
            return new List<TriatlonModel.Proba>();
        }

        public TriatlonModel.TipProba GetProbaForArbitru(TriatlonModel.Arbitru arbitru)
        {
            var requestBuilder = ProtoUtils.CreateEmptyRequest(Request.Types.Type.GetProbaForArbitru);

            requestBuilder.Arbitru = ProtoUtils.ToProto(arbitru);

            SendRequest(requestBuilder);
            Response response = ReadResponse();

            if (response.Type == Response.Types.Type.Ok ||
                response.Type == Response.Types.Type.GetProbaForArbitru)
            {
                if (response.TipProba != null)
                {
                    return ProtoUtils.FromProto(response.TipProba);
                }
            }
            throw new TriatlonException("No proba found for arbitru");
        }

        TriatlonModel.Arbitru ITriatlonServices.FindArbitruByUsernameAndPassword(string username, string password)
        {
            try
            {
                InitializeConnection();

                Request request = ProtoUtils.CreateFindArbitruRequest(username, password);
                SendRequest(request);
                Response response = ReadResponse();

                if (response.Type == Response.Types.Type.Ok ||
                    response.Type == Response.Types.Type.FindArbitru)
                {
                    if (response.Arbitru != null)
                    {
                        return ProtoUtils.FromProto(response.Arbitru);
                    }
                }
                return null;
            }
            catch (Exception e)
            {
                logger.Error($"Error finding arbitru: {e.Message}");
                return null;
            }
        }

    }
}
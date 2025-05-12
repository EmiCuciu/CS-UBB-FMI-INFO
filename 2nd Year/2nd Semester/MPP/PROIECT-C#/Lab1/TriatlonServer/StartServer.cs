using System;
using System.Configuration;
using System.Net.Sockets;
using log4net;
using TriatlonModel;
using TriatlonNetworking;
using TriatlonNetworking.jsonprotocol;
using TriatlonNetworking.protobufprotocol;
using TriatlonPersistance;
using TriatlonServer;
using TriatlonServicess;

namespace TriatlonServer
{
    public class StartServer
    {
        private static String DEFAULT_IP = "127.0.0.1";
        private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));

        public static void Main(string[] args)
        {
            log.Info("Starting server...");
            Console.WriteLine("Server starting...");


            // Initialize repositories
            IArbitruRepository arbitruRepository = new ArbitruRepository();
            IRezultatRepository rezultatRepository = new RezultatRepository();
            IParticipantRepository participantRepository = new ParticipantRepository();
            IProbaRepository probaRepository = new ProbaRepository();

            // Create service implementation
            ITriatlonServices serviceImpl = new TriatlonServicesImpl(
                arbitruRepository, rezultatRepository, participantRepository, probaRepository);

            Console.WriteLine("Server initialized.");

            // Get server port from configuration
            int serverPort = GetServerPort();

            Console.WriteLine("Server port: " + serverPort);

            // Start server
            log.Info($"Starting server on {DEFAULT_IP}:{serverPort}");
            Console.WriteLine($"Starting server on {DEFAULT_IP}:{serverPort}");

            //? SUNT AICI :)
            // SerialChatServer server = new SerialChatServer(DEFAULT_IP, serverPort, serviceImpl);

            // SUNT AICI :)
            ProtoV3ChatServer server = new ProtoV3ChatServer(DEFAULT_IP, serverPort, serviceImpl);

            log.Info("Server started.");
            Console.WriteLine("Server started.");
            server.Start();
        }

        private static int GetServerPort()
        {
            int defaultPort = 55556;
            string serverPort = ConfigurationManager.AppSettings["port"];
            if (int.TryParse(serverPort, out int port))
            {
                return port;
            }
            log.Warn($"Could not parse server port from configuration, using default: {defaultPort}");
            return defaultPort;
        }
    }

    public class SerialChatServer : ConcurrentServer
    {
        private ITriatlonServices server;
        private static readonly ILog log = LogManager.GetLogger(typeof(SerialChatServer));

        public SerialChatServer(string host, int port, ITriatlonServices server) : base(host, port)
        {
            this.server = server;
            log.Info($"Created server on {host}:{port}");
        }

        protected override Thread createWorker(TcpClient client)
        {
            TriatlonClientJsonWorker worker = new TriatlonClientJsonWorker(server, client);
            Thread thread = new Thread(worker.Run);
            log.Info($"Created worker for client {client.Client.RemoteEndPoint}");
            return thread;
        }
    }

    public class ProtoV3ChatServer : ConcurrentServer
    {
        private ITriatlonServices services;
        private ProtoTriatlonWorker worker;
        public ProtoV3ChatServer(string host, int port, ITriatlonServices services)
            : base(host, port)
        {
            this.services = services;
            Console.WriteLine("ProtoChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ProtoTriatlonWorker(services, client);
            return new Thread(new ThreadStart(worker.Run));
        }
    }
}
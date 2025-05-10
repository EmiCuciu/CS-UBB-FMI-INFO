using System;
using System.Configuration;
using System.IO;
using System.Reflection;
using System.Windows.Forms;
using Lab1.Domain;
using Lab1.Repository;
using Lab1.Service;
using log4net;
using log4net.Config;

namespace Lab1
{
    internal class Program
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(Program));

        [STAThread]
        static void Main(string[] args)
        {
            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

            log.Info("Application started");

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            string connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;

            // Initialize repositories
            var arbitruRepository = new ArbitruRepository();
            var participantRepository = new ParticipantRepository();
            var rezultatRepository = new RezultatRepository();
            var probaRepository = new ProbaRepository();

            // Initialize services
            var authService = new AuthentificationService(arbitruRepository);
            var participantService = new ParticipantService(participantRepository, rezultatRepository);
            var probaService = new ProbaService(probaRepository);
            var rezultatService = new RezultatService(rezultatRepository);

            // Create and run the main form
            var loginForm = new LoginForm(authService, participantService, probaService, rezultatService);
            Application.Run(loginForm);
        }
    }
}
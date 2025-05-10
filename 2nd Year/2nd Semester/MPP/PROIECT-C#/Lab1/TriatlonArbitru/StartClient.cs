using System;
using System.Configuration;
using System.Windows.Forms;
using Triatlon.Network.ProtobufProtocol;
using TriatlonArbitru;
using TriatlonNetworking.jsonprotocol;

namespace TriatlonClient
{
    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            try
            {
                // Configure log4net
                var logRepository = log4net.LogManager.GetRepository(System.Reflection.Assembly.GetEntryAssembly());
                log4net.Config.XmlConfigurator.Configure(logRepository, new System.IO.FileInfo("log4net.config"));

                // Read server settings from config
                string host = ConfigurationManager.AppSettings["host"];
                if (string.IsNullOrEmpty(host))
                {
                    host = "127.0.0.1";
                }

                int port = 55556;
                string portString = ConfigurationManager.AppSettings["port"];
                if (!string.IsNullOrEmpty(portString))
                {
                    int.TryParse(portString, out port);
                }

                Console.WriteLine($"Using server {host}:{port}");

                //? SUNT AICI :)
                // var server = new TriatlonServerJsonProxy(host, port);

                //? SUNT AICI :)
                var server = new ProtoTriatlonProxy(host, port);

                Application.Run(new Login(server));
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
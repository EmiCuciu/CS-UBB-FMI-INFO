using System;
using System.Configuration;
using System.Data;
using System.Threading;
using Microsoft.Data.SqlClient;

namespace Tranzactii
{
    class Program
    {
        private static string _connection = ConfigurationManager.ConnectionStrings["connectionString"].ConnectionString;
        private static int _maxIncercari = 3;

        static void Main()
        {
            // Thread t1 = new Thread(() => ExecuteWithRetry("T1_DEADLOCK"));
            // Thread t2 = new Thread(() => ExecuteWithRetry("T2_DEADLOCK"));
            //
            // t1.Start();
            // t2.Start();
            //
            // t1.Join();
            // t2.Join();
            // Console.WriteLine("Executie finalizata.");


            Thread t1 = new Thread(() => ExecuteWithRetry("T1_DEADLOCK_C#"));
            Thread t2 = new Thread(() => ExecuteWithRetry("T2_DEADLOCK_C#"));

            t1.Start();
            Thread.Sleep(50);
            t2.Start();

            t1.Join();
            t2.Join();
            Console.WriteLine("Executie finalizata.");


        }

        static void ExecuteWithRetry(string procedura)
        {
            int incercari = 0;
            bool success = false;

            while (!success && incercari < _maxIncercari)
            {
                try
                {
                    using (SqlConnection sqlConnection = new SqlConnection(_connection))
                    {
                        sqlConnection.Open();
                        using (SqlCommand command = new SqlCommand(procedura, sqlConnection))
                        {
                            command.CommandType = CommandType.StoredProcedure;
                            command.ExecuteNonQuery();
                        }
                    }

                    success = true;
                    Console.WriteLine($"{procedura} executata cu succes.");
                }
                catch (SqlException e)
                {
                    incercari++;

                    if (e.Number == 1205) // Deadlock
                    {
                        Console.WriteLine($"{procedura} DEADLOCK detectat. Reincercare {incercari}/{_maxIncercari} ...");
                        Thread.Sleep(1000);
                    }
                    else
                    {
                        Console.WriteLine($"{procedura} Eroare SQL: {e.Message}");
                        break;
                    }
                }
            }

            if (!success)
            {
                Console.WriteLine($"{procedura} a esuat");
            }
        }
    }
}
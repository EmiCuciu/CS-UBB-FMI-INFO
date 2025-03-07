using Microsoft.Data.SqlClient;

internal class Program
{
    static void Main(string[] args)
    {
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.Clear();
        Console.WriteLine("Hello, World of ADO.NET!");

        string connectionString =
            @"Server=DESKTOP-LI67DLG\SQLEXPRESS;Database=S1;Integrated Security=true;TrustServerCertificate=true";

        try
        {
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                Console.WriteLine("Starea conexiunii: {0}", con.State);
                //deschidem conexiunea
                con.Open();
                Console.WriteLine("Starea conexiunii: {0}", con.State);

                //adaugarea datelor
                SqlCommand insertCommand =
                    new SqlCommand(
                        "INSERT INTO Produse (nume, pret, producator) VALUES (@nume1, @pret1, @producator1), (@nume2, @pret2, @producator2);",
                        con);
                insertCommand.Parameters.AddWithValue("@nume1", "burete");
                insertCommand.Parameters.AddWithValue("@pret1", 10);
                insertCommand.Parameters.AddWithValue("@producator1", "Lidl");

                insertCommand.Parameters.AddWithValue("@nume2", "shaorma");
                insertCommand.Parameters.AddWithValue("@pret2", 36);
                insertCommand.Parameters.AddWithValue("@producator2", "BigBelly");

                // int insertRowCount = insertCommand.ExecuteNonQuery();
                // Console.WriteLine("Insert Row Count: {0}", insertRowCount);

                // citirea datelor
                Console.WriteLine("Citirea si afisarea dateolor");
                SqlCommand selectCommand = new SqlCommand("SELECT nume, pret, producator FROM Produse", con);

                SqlDataReader reader = selectCommand.ExecuteReader();

                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        Console.WriteLine("{0}\t{1}\t{2}", reader.GetString(0), reader.GetDouble(1),
                            reader.GetString(2));
                    }
                }
                reader.Close();
            }
        }
        catch (Exception e)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("Mesajul erorii: {0}", e.Message);
        }
    }
}
using Microsoft.Data.SqlClient;
using System.Linq.Expressions;
namespace ConsoleApp222SGBD2025
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.BackgroundColor = ConsoleColor.DarkCyan;
            Console.ForegroundColor = ConsoleColor.White;
            Console.Clear();
            Console.WriteLine("Hello, World of ADO.NET!");
            //este necesara modificarea connectionString-ului pentru a va putea conecta la instanta SQL Server proprie
            string connectionString = @"Server=DESKTOP-T42EI2V;Database=S1222SGBD2025;Integrated Security=
            true;TrustServerCertificate=true;";
            try
            {
                using(SqlConnection con = new SqlConnection(connectionString))
                {
                    Console.WriteLine("Starea conexiunii: {0}", con.State);
                    //deschidem conexiunea
                    con.Open();
                    Console.WriteLine("Starea conexiunii: {0}", con.State);
                    //adaugarea datelor
                    SqlCommand insertCommand = new SqlCommand("INSERT INTO Produse (nume, pret, " +
                        "producator) VALUES (@nume1, @pret1, @prod1), (@nume2, @pret2, @prod2);", con);
                    insertCommand.Parameters.AddWithValue("@nume1", "burete");
                    insertCommand.Parameters.AddWithValue("@pret1", 10);
                    insertCommand.Parameters.AddWithValue("@prod1", "Lidl");
                    insertCommand.Parameters.AddWithValue("@nume2", "shaorma");
                    insertCommand.Parameters.AddWithValue("@pret2", 15);
                    insertCommand.Parameters.AddWithValue("@prod2", "Rosa");
                    int insertRowCount = insertCommand.ExecuteNonQuery();
                    Console.WriteLine("Insert Row Count: {0}", insertRowCount);
                    //citirea datelor
                    Console.WriteLine("Citirea si afisarea datelor");
                    SqlCommand selectCommand = new SqlCommand("SELECT nume, pret, producator FROM " +
                        "Produse;", con);
                    SqlDataReader reader = selectCommand.ExecuteReader();
                    if(reader.HasRows)
                    {
                        while(reader.Read())
                        {
                            Console.WriteLine("{0}\t{1}\t{2}", reader.GetString(0), reader.GetDouble(1),
                                reader.GetString(2));
                        }
                    }
                    reader.Close();
                    //actualizarea datelor
                    SqlCommand updateCommand = new SqlCommand("UPDATE Produse SET pret=@pretnou WHERE " +
                        "nume=@nume;", con);
                    updateCommand.Parameters.AddWithValue("@pretnou", 24.99);
                    updateCommand.Parameters.AddWithValue("@nume", "shaorma");
                    /*putem executa metoda ExecuteNonQuery si fara a stoca number of rows affected 
                    intr-o variabila locala, de exemplu:
                    updateCommand.ExecuteNonQuery();
                    */
                    int updateRowCount = updateCommand.ExecuteNonQuery();
                    Console.WriteLine("Update Row Count: {0}", updateRowCount);
                    //stergerea datelor
                    SqlCommand deleteCommand = new SqlCommand("DELETE FROM Produse WHERE nume=@nume;", con);
                    deleteCommand.Parameters.AddWithValue("@nume", "burete");
                    int deleteRowCount = deleteCommand.ExecuteNonQuery();
                    Console.WriteLine("Delete Row Count: {0}", deleteRowCount);
                    //citirea si afisarea datelor dupa actualizare si stergere
                    Console.WriteLine("Citirea si afisarea datelor dupa actualizare si stergere");
                    reader = selectCommand.ExecuteReader();
                    if(reader.HasRows)
                    {
                        while(reader.Read())
                        {
                            Console.WriteLine("{0}\t{1}\t{2}", reader.GetString(0), reader.GetDouble(1),
                                reader.GetString(2));
                        }
                    }
                    reader.Close();

                }
            }
            catch(Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul exceptiei: {0}", ex.Message);
            }
        }
    }
}

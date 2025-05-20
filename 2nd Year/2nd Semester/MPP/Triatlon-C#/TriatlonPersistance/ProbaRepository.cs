using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SQLite;
using TriatlonModel;
using log4net;

namespace TriatlonPersistance
{
    public class ProbaRepository : IProbaRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ProbaRepository));
        public readonly string connectionString;

        public ProbaRepository()
        {
            connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
        }
        public void Delete(int id)
        {
            logger.Debug("Enter deleting Proba with id: " + id);
            string sql = "DELETE FROM Proba WHERE id=@id";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", id);
                        command.ExecuteNonQuery();
                        logger.Debug("Deleted Proba with id: " + id);
                    }
                    connection.Close();
                }
                logger.Debug("Exit deleting Proba with id: " + id);
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error deleting Proba with id: " + id, ex);
            }
        }

        public List<Proba> FindAll()
        {
            logger.Debug("Enter FindAll Proba");
            List<Proba> probe = new List<Proba>();
            string sql = "SELECT * FROM Proba";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        using (SQLiteDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                int arbitru_id = reader.GetInt32(2);
                                Arbitru arbitru = new ArbitruRepository().FindOne(arbitru_id);
                                String tipProbaStr = reader.GetString(1);
                                TipProba tipProba = (TipProba)Enum.Parse(typeof(TipProba), tipProbaStr);
                                Proba proba = new Proba(reader.GetInt32(0), tipProba, arbitru);
                                probe.Add(proba);
                                logger.Debug("Found Proba: " + proba);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error FindAll Proba", ex);
            }
            logger.Debug("Exit FindAll Proba");
            return probe;
        }

        public Proba FindOne(int id)
        {
            logger.Debug("Enter FindOne Proba with id: " + id);
            string sql = "SELECT * FROM Proba WHERE id=@id";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", id);
                        using (SQLiteDataReader reader = command.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                int arbitru_id = reader.GetInt32(2);
                                Arbitru arbitru = new ArbitruRepository().FindOne(arbitru_id);
                                String tipProbaStr = reader.GetString(1);
                                TipProba tipProba = (TipProba)Enum.Parse(typeof(TipProba), tipProbaStr);
                                Proba proba = new Proba(reader.GetInt32(0), tipProba, arbitru);
                                logger.Debug("Found Proba: " + proba);
                                return proba;
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error FindOne Proba with id: " + id, ex);
            }
            return null;
        }

        public void Save(Proba entity)
        {
            logger.Debug("Saving Proba " + entity);
            string sql = "INSERT INTO Proba (tip_proba, arbitru_id) VALUES (@TipProba, @ArbitruId)";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {

                        command.Parameters.AddWithValue("@TipProba", entity.TipProba.ToString());
                        command.Parameters.AddWithValue("@ArbitruId", entity.Arbitru.Id);

                        int affectedRows = command.ExecuteNonQuery();
                        if (affectedRows > 0)
                        {
                            command.CommandText = "SELECT last_insert_rowid()";
                            entity.Id = Convert.ToInt32(command.ExecuteScalar());
                        }
                        logger.Debug("Saved Proba " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error saving Proba " + entity, ex);
            }
            logger.Debug("Exit Save Proba " + entity);
        }

        public void Update(Proba entity)
        {
            logger.Debug("Enter Update Proba " + entity);
            string sql = "UPDATE Proba SET tip_proba=@TipProba, arbitru_id=@ArbitruId WHERE id=@id";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@TipProba", entity.TipProba.ToString());
                        command.Parameters.AddWithValue("@ArbitruId", entity.Arbitru.Id);
                        command.Parameters.AddWithValue("@id", entity.Id);
                        command.ExecuteNonQuery();
                        logger.Debug("Updated Proba " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error updating Proba " + entity, ex);
            }
        }
    }
}

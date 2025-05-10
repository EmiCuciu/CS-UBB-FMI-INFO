using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SQLite;
using TriatlonModel;
using log4net;

namespace TriatlonPersistance
{
    public class RezultatRepository : IRezultatRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(RezultatRepository));
        public readonly string connectionString;

        public RezultatRepository()
        {
            connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
        }
        public void Delete(int id)
        {
            logger.Debug("Enter deleting Rezultat with id: " + id);
            string sql = "DELETE FROM Rezultat WHERE id=@id";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", id);
                        command.ExecuteNonQuery();
                        logger.Debug("Deleted Rezultat with id: " + id);
                    }
                    connection.Close();
                }
                logger.Debug("Exit deleting Rezultat with id: " + id);
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error deleting Rezultat with id: " + id, ex);
            }
        }

        public List<Rezultat> FindAll()
        {
            logger.Debug("Enter FindAll Rezultat");
            List<Rezultat> rezultate = new List<Rezultat>();
            string sql = "SELECT * FROM Rezultat";
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
                                int participant_id = reader.GetInt32(1);
                                Participant participant = new ParticipantRepository().FindOne(participant_id);
                                int arbitru_id = reader.GetInt32(2);
                                Arbitru arbitru = new ArbitruRepository().FindOne(arbitru_id);
                                String tipProbaStr = reader.GetString(3);
                                TipProba tipProba = (TipProba)Enum.Parse(typeof(TipProba), tipProbaStr);
                                int points = reader.GetInt32(4);
                                Rezultat rezultat = new Rezultat(reader.GetInt32(0), participant, arbitru, tipProba, points);
                                rezultate.Add(rezultat);
                                logger.Debug("Found Rezultat: " + rezultat);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error finding Rezultate", ex);
            }
            logger.Debug("Exit FindAll Rezultat");
            return rezultate;
        }

        public Rezultat FindOne(int id)
        {
            logger.Debug("Enter FindOne Rezultat with id: " + id);
            string sql = "SELECT * FROM Rezultat WHERE id=@id";
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
                                int participant_id = reader.GetInt32(1);
                                Participant participant = new ParticipantRepository().FindOne(participant_id);
                                int arbitru_id = reader.GetInt32(2);
                                Arbitru arbitru = new ArbitruRepository().FindOne(arbitru_id);
                                String tipProbaStr = reader.GetString(3);
                                TipProba tipProba = (TipProba)Enum.Parse(typeof(TipProba), tipProbaStr);
                                int points = reader.GetInt32(4);
                                Rezultat rezultat = new Rezultat(reader.GetInt32(0), participant, arbitru, tipProba, points);
                                logger.Debug("Found Rezultat: " + rezultat);
                                return rezultat;
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error finding Rezultat with id: " + id, ex);
            }
            logger.Debug("Exit FindOne Rezultat with id: " + id);
            return null;
        }

        public void Save(Rezultat entity)
        {
            logger.Debug("Enter Save Rezultat: " + entity);
            string sql = "INSERT INTO Rezultat (participant_id, arbitru_id, tip_proba, punctaj) VALUES (@participant_id, @arbitru_id, @tipProba, @points)";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@participant_id", entity.Participant.Id);
                        command.Parameters.AddWithValue("@arbitru_id", entity.Arbitru.Id);
                        command.Parameters.AddWithValue("@tipProba", entity.TipProba.ToString());
                        command.Parameters.AddWithValue("@points", entity.Points);
                        int affectedRows = command.ExecuteNonQuery();
                        if (affectedRows > 0)
                        {
                            command.CommandText = "SELECT last_insert_rowid()";
                            entity.Id = Convert.ToInt32(command.ExecuteScalar());
                        }
                        logger.Debug("Saved Rezultat: " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error saving Rezultat: " + entity, ex);
            }
            logger.Debug("Exit Save Rezultat: " + entity);
        }

        public void Update(Rezultat entity)
        {
            logger.Debug("Enter Update Rezultat: " + entity);
            string sql = "UPDATE Rezultat SET participant_id=@participant_id, arbitru_id=@arbitru_id, tip_proba=@tipProba, punctaj=@points WHERE id=@id";
            try
            {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString))
                {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@participant_id", entity.Participant.Id);
                        command.Parameters.AddWithValue("@arbitru_id", entity.Arbitru.Id);
                        command.Parameters.AddWithValue("@tipProba", entity.TipProba.ToString());
                        command.Parameters.AddWithValue("@points", entity.Points);
                        command.Parameters.AddWithValue("@id", entity.Id);
                        command.ExecuteNonQuery();
                        logger.Debug("Updated Rezultat: " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex)
            {
                logger.Error("Error updating Rezultat: " + entity, ex);
            }
        }
    }
}

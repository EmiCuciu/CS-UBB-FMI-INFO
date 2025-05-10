using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SQLite;
using Lab1.Domain;
using log4net;

namespace Lab1.Repository {
    public class ParticipantRepository : IParticipantRepository {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ParticipantRepository));
        public readonly string connectionString;

        public ParticipantRepository() {
            connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
        }

        public void Delete(int id) {
            logger.Debug("Enter deleting Participant with id: " + id);
            string sql = "DELETE FROM Participant WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", id);
                        command.ExecuteNonQuery();
                    }
                    connection.Close();
                }
                logger.Debug("Exit deleting Participant with id: " + id);
            }
            catch (SQLiteException ex) {
                logger.Error("Error deleting Participant with id: " + id, ex);
            }
        }

        public List<Participant> FindAll() {
            logger.Debug("Enter FindAll Participanti: ");
            List<Participant> participanti = new List<Participant>();
            string sql = "SELECT * FROM Participant";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        using (SQLiteDataReader reader = command.ExecuteReader()) {
                            while (reader.Read()) {
                                int id = reader.GetInt32(0);
                                string firstName = reader.GetString(1);
                                string lastName = reader.GetString(2);
                                Participant participant = new Participant(id, firstName, lastName);
                                participanti.Add(participant);
                                logger.Debug("Found Participant: " + participant);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex) {
                logger.Error("Error FindAll Participanti", ex);
            }
            return participanti;
        }

        public Participant FindOne(int id) {
            logger.Debug("Enter FindOne Participant with id: " + id);
            string sql = "SELECT * FROM Participant WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", id);
                        using (SQLiteDataReader reader = command.ExecuteReader()) {
                            if (reader.Read()) {
                                string firstName = reader.GetString(1);
                                string lastName = reader.GetString(2);
                                Participant participant = new Participant(id, firstName, lastName);
                                logger.Debug("Found Participant: " + participant);
                                return participant;
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex) {
                logger.Error("Error FindOne Participant with id: " + id, ex);
            }
            return null;
        }

        public void Save(Participant entity) {
            logger.Debug("Enter Save Participant: " + entity);
            string sql = "INSERT INTO Participant (first_name, last_name) VALUES (@firstName, @lastName)";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@firstName", entity.FirstName);
                        command.Parameters.AddWithValue("@lastName", entity.LastName);

                        int affectedRows = command.ExecuteNonQuery();
                        if (affectedRows > 0) {
                            command.CommandText = "SELECT last_insert_rowid()";
                            entity.Id = Convert.ToInt32(command.ExecuteScalar());
                        }
                        logger.Debug("Saved Participant: " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex) {
                logger.Error("Error saving Participant: " + entity, ex);
            }
        }

        public void Update(Participant entity) {
            logger.Debug("Enter Update Participant: " + entity);
            string sql = "UPDATE Participant SET first_name=@firstName, last_name=@lastName WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", entity.Id);
                        command.Parameters.AddWithValue("@firstName", entity.FirstName);
                        command.Parameters.AddWithValue("@lastName", entity.LastName);
                        command.ExecuteNonQuery();
                        logger.Debug("Updated Participant: " + entity);
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex) {
                logger.Error("Error updating Participant: " + entity, ex);
            }
        }
    }
}

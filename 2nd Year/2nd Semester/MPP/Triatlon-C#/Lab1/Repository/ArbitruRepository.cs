using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SQLite;
using Lab1.Domain;
using log4net;

namespace Lab1.Repository {
    public class ArbitruRepository : IArbitruRepository {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ArbitruRepository));
        public readonly string connectionString;

        public ArbitruRepository() {
            connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
        }

        public void Delete(int id) {
            logger.Debug("Enter deleting Arbitru with id: " + id);
            string sql = "DELETE FROM Arbitru WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", id);
                        command.ExecuteNonQuery();
                    }
                    connection.Close();
                }
                logger.Debug("Exit deleting Arbitru with id: " + id);
            }
            catch (SQLiteException ex) {
                logger.Error("Error deleting Arbitru with id: " + id, ex);
            }
        }

        public List<Arbitru> FindAll() {
            logger.Debug("Enter FindAll Arbitru");
            List<Arbitru> arbitri = new List<Arbitru>();
            string sql = "SELECT * FROM Arbitru";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        using (SQLiteDataReader reader = command.ExecuteReader()) {
                            while (reader.Read()) {
                                int id = reader.GetInt32(0);
                                string username = reader.GetString(1);
                                string password = reader.GetString(2);
                                string firstName = reader.GetString(3);
                                string lastName = reader.GetString(4);
                                Arbitru arbitru = new Arbitru(id, username, password, firstName, lastName);
                                arbitri.Add(arbitru);
                                logger.Debug("Found Arbitru: " + arbitru);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SQLiteException ex) {
                logger.Error("Error finding all Arbitru", ex);
            }
            logger.Debug("Exit FindAll Arbitru");
            return arbitri;
        }

        public Arbitru FindOne(int id) {
            logger.Debug("Enter FindOne Arbitru with id: " + id);
            Arbitru arbitru = null;
            string sql = "SELECT * FROM Arbitru WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", id);
                        using (SQLiteDataReader reader = command.ExecuteReader()) {
                            if (reader.Read()) {
                                string username = reader.GetString(1);
                                string password = reader.GetString(2);
                                string firstName = reader.GetString(3);
                                string lastName = reader.GetString(4);
                                arbitru = new Arbitru(id, username, password, firstName, lastName);
                                logger.Debug("Found Arbitru: " + arbitru);
                            }
                        }
                    }
                    connection.Close();
                }
                logger.Debug("Exit FindOne Arbitru with id: " + id);
            }
            catch (SQLiteException ex) {
                logger.Error("Error finding Arbitru with id: " + id, ex);
            }
            return arbitru;
        }

        public void Save(Arbitru entity) {
            logger.Debug("Enter Save Arbitru: " + entity);

            string sql = "INSERT INTO Arbitru (username, password, first_name, last_name) VALUES (@username, @password, @firstName, @lastName)";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@username", entity.Username);
                        command.Parameters.AddWithValue("@password", entity.Password);
                        command.Parameters.AddWithValue("@firstName", entity.FirstName);
                        command.Parameters.AddWithValue("@lastName", entity.LastName);
                        int affectedRows = command.ExecuteNonQuery();
                        if (affectedRows > 0) {
                            command.CommandText = "SELECT last_insert_rowid()";
                            entity.Id = Convert.ToInt32(command.ExecuteScalar());
                        }
                        logger.Debug("Saved Arbitru: " + entity);
                    }
                    connection.Close();
                }
                logger.Debug("Exit Save Arbitru: " + entity);
            }
            catch (SQLiteException ex) {
                logger.Error("Error saving Arbitru: " + entity, ex);
            }
        }



        public void Update(Arbitru entity) {
            logger.Debug("Enter Update Arbitru: " + entity);
            string sql = "UPDATE Arbitru SET username=@username, password=@password, first_name=@firstName, last_name=@lastName WHERE id=@id";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@id", entity.Id);
                        command.Parameters.AddWithValue("@username", entity.Username);
                        command.Parameters.AddWithValue("@password", entity.Password);
                        command.Parameters.AddWithValue("@firstName", entity.FirstName);
                        command.Parameters.AddWithValue("@lastName", entity.LastName);
                        command.ExecuteNonQuery();
                    }
                    connection.Close();
                }
                logger.Debug("Exit Update Arbitru: " + entity);
            }
            catch (SQLiteException ex) {
                logger.Error("Error updating Arbitru: " + entity, ex);
            }
        }

        public Arbitru FindBy(string username, string password)
        {
            logger.Debug("Enter FindBy Arbitru with username: " + username + " and password: " + password);
            Arbitru arbitru = null;
            string sql = "SELECT * FROM Arbitru WHERE username=@username AND password=@password";
            try {
                using (SQLiteConnection connection = new SQLiteConnection(connectionString)) {
                    connection.Open();
                    using (SQLiteCommand command = new SQLiteCommand(sql, connection)) {
                        command.Parameters.AddWithValue("@username", username);
                        command.Parameters.AddWithValue("@password", password);
                        using (SQLiteDataReader reader = command.ExecuteReader()) {
                            if (reader.Read()) {
                                int id = reader.GetInt32(0);
                                string firstName = reader.GetString(3);
                                string lastName = reader.GetString(4);
                                arbitru = new Arbitru(id, username, password, firstName, lastName);
                                logger.Debug("Found Arbitru: " + arbitru);
                            }
                        }
                    }
                    connection.Close();
                }
                logger.Debug("Exit FindBy Arbitru with username: " + username + " and password: " + password);
            }
            catch (SQLiteException ex) {
                logger.Error("Error finding Arbitru with username: " + username + " and password: " + password, ex);
            }
            return arbitru;
        }
    }
}

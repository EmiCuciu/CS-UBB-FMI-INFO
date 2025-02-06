package org.example.ati.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ATI";
    private static final String USER = "postgres";
    private static final String PASSWORD = "emi12345";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Nu s-a putut realiza conexiunea la baza de date");
        }
    }
}
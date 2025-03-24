package com.example.laboratoriss.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static final Logger logger = LogManager.getLogger();
    private Properties jdbcProps;
    private Connection instance = null;

    public JdbcUtils(Properties props) {
        this.jdbcProps = props;
    }

    public Connection getConnection() {
        try {
            // Check if connection is null or closed
            if (instance == null || instance.isClosed()) {
                // Get database properties
                String url = jdbcProps.getProperty("jdbc.url");
                if (url == null || url.isEmpty()) {
                    throw new SQLException("Database URL is missing from properties");
                }

                // Create a new connection
                logger.info("Initializing database connection to {}", url);
                instance = DriverManager.getConnection(url);
                instance.setAutoCommit(true);
            }
            return instance;
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}
package com.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private final Properties jdbcProps;

    private static final Logger logger = LogManager.getLogger();

    public JdbcUtils(Properties props) {
        jdbcProps = props;
        logger.info("JdbcUtils initialized successfully");
    }

    private Connection getNewConnection() {
        logger.traceEntry();

        String url = jdbcProps.getProperty("chat.jdbc.url");
        String user = jdbcProps.getProperty("chat.jdbc.user");
        String pass = jdbcProps.getProperty("chat.jdbc.pass");

        //logger.info("trying to connect to database ... {}", url);
        //logger.info("user: {}", user);
        //logger.info("pass: {}", pass);

        Connection connection = null;
        try {
            if (user != null && pass != null) {
                connection = DriverManager.getConnection(url, user, pass);
            } else
                connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection" + e);
        }
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        Connection conn = getNewConnection();
        logger.traceExit();
        return conn;
    }
}

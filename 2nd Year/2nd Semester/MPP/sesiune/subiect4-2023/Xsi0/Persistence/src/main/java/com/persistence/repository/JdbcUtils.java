package com.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private Properties jdbcProps;
    public static final Logger logger = LogManager.getLogger();
    private Connection instance = null;

    public JdbcUtils(Properties props) {
        jdbcProps = props;
    }

    private Connection getNewConnection() {
        logger.traceEntry();
        String driver = jdbcProps.getProperty("jdbc.driver");
        String url = jdbcProps.getProperty("jdbc.url");
        String user = jdbcProps.getProperty("jdbc.user");
        String pass = jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("pass: {}", pass);
        Connection con = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver " + e);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return con;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error Database: " + e);
        }
        logger.traceExit(instance);
        return instance;
    }
}

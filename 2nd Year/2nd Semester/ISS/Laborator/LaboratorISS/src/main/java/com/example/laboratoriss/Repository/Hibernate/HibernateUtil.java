package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Properties properties) {
        if ((sessionFactory == null) || (sessionFactory.isClosed())) {
            try {
                Configuration configuration = new Configuration();

                Properties hibernateProps = new Properties();
                hibernateProps.put("hibernate.connection.driver_class", "org.sqlite.JDBC");
                hibernateProps.put("hibernate.connection.url", properties.getProperty("chat.jdbc.url"));
                hibernateProps.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
                hibernateProps.put("hibernate.show_sql", "true");
                hibernateProps.put("hibernate.format_sql", "true");
                hibernateProps.put("hibernate.hbm2ddl.auto", "update");

                configuration.setProperties(hibernateProps);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Medicament.class);
                configuration.addAnnotatedClass(ComandaItem.class);
                configuration.addAnnotatedClass(Comanda.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                logger.error("Error initializing Hibernate: {}", e.getMessage(), e);
                throw new RuntimeException("Error initializing Hibernate: " + e.getMessage(), e);
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

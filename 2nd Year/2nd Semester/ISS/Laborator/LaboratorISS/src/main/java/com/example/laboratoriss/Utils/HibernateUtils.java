package com.example.laboratoriss.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static final Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure() // Uses hibernate.cfg.xml by default
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .buildMetadata()
                        .buildSessionFactory();

                logger.info("Hibernate SessionFactory initialized");
            } catch (Exception e) {
                logger.error("SessionFactory creation failed", e);
                throw new RuntimeException("Failed to initialize Hibernate", e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
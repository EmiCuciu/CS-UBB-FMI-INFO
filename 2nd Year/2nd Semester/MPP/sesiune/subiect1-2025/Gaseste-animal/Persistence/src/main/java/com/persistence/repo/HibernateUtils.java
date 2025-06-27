package com.persistence.repo;

import com.model.Joc;
import com.model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory == null) || (sessionFactory.isClosed()))
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Joc.class)
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(com.model.Configuratie.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory(){
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
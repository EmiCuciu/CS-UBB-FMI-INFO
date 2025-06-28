package persistence.utils;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import domain.Jucator;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    //.addAnnotatedClass(Joc.class)
                    .addAnnotatedClass(Jucator.class)
                    //.addAnnotatedClass(Configuratie.class)
                    //.addAnnotatedClass(Incercare.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}

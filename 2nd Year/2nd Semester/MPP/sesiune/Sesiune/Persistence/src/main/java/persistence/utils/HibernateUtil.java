package persistence.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import domain.Jucator;
import domain.Intrebare;
import domain.Joc;
import domain.RaspunsJucator;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(Jucator.class)
                    .addAnnotatedClass(Intrebare.class)
                    .addAnnotatedClass(Joc.class)
                    .addAnnotatedClass(RaspunsJucator.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
package persistence.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import domain.Configuratie;
import domain.Cuvant;
import domain.Incercare;
import domain.Joc;
import domain.Jucator;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(Jucator.class)
                    .addAnnotatedClass(Cuvant.class)
                    .addAnnotatedClass(Configuratie.class)
                    .addAnnotatedClass(Joc.class)
                    .addAnnotatedClass(Incercare.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
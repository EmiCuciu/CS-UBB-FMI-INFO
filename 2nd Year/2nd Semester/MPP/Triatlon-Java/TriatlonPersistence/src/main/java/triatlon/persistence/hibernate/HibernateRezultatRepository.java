package triatlon.persistence.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import triatlon.model.Rezultat;
import triatlon.persistence.IRezultatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateRezultatRepository implements IRezultatRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateRezultatRepository(Properties properties) {
        logger.info("Initializing HibernateRezultatRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Rezultat findOne(Integer integer) {
        logger.traceEntry("finding Rezultat with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rezultat.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Rezultat with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Rezultat> findAll() {
        logger.traceEntry("finding all Rezultate");
        List<Rezultat> rezultate = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            rezultate = session.createQuery("from Rezultat", Rezultat.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Rezultate: {}", e.getMessage());
        }
        logger.traceExit("Found {} Rezultate", rezultate.size());
        return rezultate;
    }

    @Override
    public void save(Rezultat entity) {
        logger.traceEntry("saving Rezultat {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error saving Rezultat: {}", e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Rezultat with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Rezultat rezultat = session.get(Rezultat.class, integer);
                if (rezultat != null) {
                    session.remove(rezultat);
                    tx.commit();
                } else {
                    logger.warn("Rezultat with id {} not found", integer);
                }
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error deleting Rezultat with id {}: {}", integer, e.getMessage());
            }
        }
    }

    @Override
    public void update(Rezultat entity) {
        logger.traceEntry("updating Rezultat {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error updating Rezultat {}: {}", entity, e.getMessage());
            }
        }
    }
}
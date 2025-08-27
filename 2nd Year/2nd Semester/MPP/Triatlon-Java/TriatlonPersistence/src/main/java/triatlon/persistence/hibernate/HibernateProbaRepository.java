package triatlon.persistence.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import triatlon.model.Proba;
import triatlon.persistence.IProbaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateProbaRepository implements IProbaRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateProbaRepository(Properties properties) {
        logger.info("Initializing HibernateProbaRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Proba findOne(Integer integer) {
        logger.traceEntry("finding Proba with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            return session.get(Proba.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Proba with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry("finding all Probe");
        List<Proba> probe = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            probe = session.createQuery("from Proba", Proba.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Probe: {}", e.getMessage());
        }
        return probe;
    }

    @Override
    public void save(Proba entity) {
        logger.traceEntry("saving Proba {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error saving Proba {}: {}", entity, e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Proba with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Proba proba = session.get(Proba.class, integer);
                if (proba != null) {
                    session.remove(proba);
                }
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error deleting Proba with id {}: {}", integer, e.getMessage());
            }
        }
    }

    @Override
    public void update(Proba entity) {
        logger.traceEntry("updating Proba {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error updating Proba {}: {}", entity, e.getMessage());
            }
        }
    }
}
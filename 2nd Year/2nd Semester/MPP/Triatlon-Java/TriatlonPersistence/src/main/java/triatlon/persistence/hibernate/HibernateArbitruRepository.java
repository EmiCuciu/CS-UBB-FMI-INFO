package triatlon.persistence.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import triatlon.model.Arbitru;
import triatlon.persistence.IArbitruRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateArbitruRepository implements IArbitruRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateArbitruRepository(Properties properties) {
        logger.info("Initializing HibernateArbitruRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Arbitru findBy(String username, String password) {
        logger.traceEntry("finding Arbitru with username {} and password {}", username, password);
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Arbitru where username = :username and password = :password", Arbitru.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding Arbitru with username {}: {}", username, e.getMessage());
            return null;
        }
    }

    @Override
    public Arbitru findOne(Integer integer) {
        logger.traceEntry("finding Arbitru with id {}", integer);

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Arbitru where id = :id", Arbitru.class)
                    .setParameter("id", integer)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding Arbitru with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Arbitru> findAll() {
        logger.traceEntry("finding all Arbitrii");
        List<Arbitru> arbitrii = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            arbitrii = session.createQuery("from Arbitru", Arbitru.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Arbitrii: {}", e.getMessage());
        }
        logger.traceExit("Found {} Arbitrii", arbitrii.size());
        return arbitrii;
    }

    @Override
    public void save(Arbitru entity) {
        logger.traceEntry("saving Arbitru {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error saving Arbitru {}: {}", entity, e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Arbitru with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Arbitru arbitru = session.find(Arbitru.class, integer);
                if (arbitru != null) {
                    session.remove(arbitru);
                    tx.commit();
                } else {
                    logger.warn("Arbitru with id {} not found", integer);
                }
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error deleting Arbitru with id {}: {}", integer, e.getMessage());
            }
        }
    }

    @Override
    public void update(Arbitru entity) {
        logger.traceEntry("updating Arbitru {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error updating Arbitru {}: {}", entity, e.getMessage());
            }
        }
    }
}

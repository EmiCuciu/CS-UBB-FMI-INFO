package triatlon.persistence.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import triatlon.model.Participant;
import triatlon.persistence.IParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateParticipantRepository implements IParticipantRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateParticipantRepository(Properties properties) {
        logger.info("Initializing HibernateParticipantRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("finding Participant with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            return session.get(Participant.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Participant with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry("finding all Participants");
        List<Participant> participants = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            participants = session.createQuery("from Participant", Participant.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Participants: {}", e.getMessage());
        }
        return participants;
    }

    @Override
    public void save(Participant entity) {
        logger.traceEntry("saving Participant {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error saving Participant {}: {}", entity, e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Participant with id {}", integer);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Participant participant = session.get(Participant.class, integer);
                if (participant != null) {
                    session.remove(participant);
                }
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error deleting Participant with id {}: {}", integer, e.getMessage());
            }
        }
    }

    @Override
    public void update(Participant entity) {
        logger.traceEntry("updating Participant {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                logger.error("Error updating Participant {}: {}", entity, e.getMessage());
            }
        }
    }
}
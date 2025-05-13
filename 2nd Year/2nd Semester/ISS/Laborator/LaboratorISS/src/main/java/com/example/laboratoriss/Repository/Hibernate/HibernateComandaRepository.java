package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IComandaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateComandaRepository implements IComandaRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateComandaRepository(Properties properties) {
        logger.info("Initializing HibernateComandaRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory(properties);
    }

    @Override
    public Comanda findOne(Integer id) {
        logger.traceEntry("finding Comanda with id {}", id);

        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Comanda WHERE id = :id", Comanda.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding Comanda with id {}: {}", id, e.getMessage());
        }
        logger.traceExit("found Comanda with id {}", id);
        return null;
    }

    @Override
    public Iterable<Comanda> findAll() {
        logger.traceEntry("finding all Comenzi");
        List<Comanda> comenzi = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            comenzi = session.createQuery("FROM Comanda", Comanda.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Comenzi: {}", e.getMessage());
        }
        logger.traceExit("found {} Comenzi", comenzi.size());
        return comenzi;
    }

    @Override
    public List<Comanda> findByUser(User user) {
        logger.traceEntry("finding Comenzi for user {}", user);
        List<Comanda> comenzi = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            comenzi = session.createQuery("FROM Comanda WHERE user.id = :userId", Comanda.class)
                    .setParameter("userId", user.getId())
                    .list();
        } catch (Exception e) {
            logger.error("Error finding Comenzi for user {}: {}", user, e.getMessage());
        }
        logger.traceExit("found {} Comenzi for user {}" + comenzi.size() + user);
        return comenzi;
    }

    @Override
    public List<Comanda> findByStatus(Status status) {
        logger.traceEntry("finding Comenzi with status {}", status);
        List<Comanda> comenzi = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            comenzi = session.createQuery("FROM Comanda WHERE status = :status", Comanda.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding Comenzi with status {}: {}", status, e.getMessage());
        }
        logger.traceExit("found {} Comenzi with status {}" + comenzi.size() + status);
        return comenzi;
    }

    @Override
    public void save(Comanda entity) {
        logger.traceEntry("saving Comanda {}", entity);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error saving Comanda {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("saved Comanda {}", entity);
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting Comanda with id {}", id);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Comanda comanda = session.find(Comanda.class, id);
                if (comanda != null) {
                    session.remove(comanda);
                    tx.commit();
                } else {
                    logger.warn("Comanda with id {} not found", id);
                }
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error deleting Comanda with id {}: {}", id, e.getMessage());
            }
        }
        logger.traceExit("deleted Comanda with id {}", id);
    }

    @Override
    public void update(Comanda entity) {
        logger.traceEntry("updating Comanda {}", entity);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error updating Comanda {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("updated Comanda {}", entity);
    }
}
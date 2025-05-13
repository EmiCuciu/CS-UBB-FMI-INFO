package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateUserRepository implements IUserRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateUserRepository(Properties properties) {
        logger.info("Initializing HibernateUserRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory(properties);
    }

    @Override
    public User findOne(Integer integer) {
        logger.traceEntry("finding User with id {}", integer);

        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE id = :id", User.class)
                    .setParameter("id", integer)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding User with id {}: {}", integer, e.getMessage());
        }
        logger.traceExit("found User with id {}", integer);
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry("finding all Users");
        List<User> users = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            users = session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Users: {}", e.getMessage());
        }
        logger.traceExit("found {} Users", users.size());
        return users;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("saving User {}", entity);

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
                logger.error("Error saving User {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("saved User {}", entity);
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting User with id {}", integer);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User user = session.find(User.class, integer);
                if (user != null) {
                    session.remove(user);
                    tx.commit();
                } else {
                    logger.warn("User with id {} not found", integer);
                }
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error deleting User with id {}: {}", integer, e.getMessage());
            }
        }
        logger.traceExit("deleted User with id {}", integer);
    }

    @Override
    public void update(User entity) {
        logger.traceEntry("updating User {}", entity);

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
                logger.error("Error updating User {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("updated User {}", entity);
    }
}

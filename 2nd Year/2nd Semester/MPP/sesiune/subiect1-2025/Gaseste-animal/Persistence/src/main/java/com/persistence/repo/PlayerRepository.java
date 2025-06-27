package com.persistence.repo;

import com.model.Player;
import com.persistence.IPlayerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PlayerRepository implements IPlayerRepository {

    private static final Logger logger = LogManager.getLogger();

    public PlayerRepository() {
        logger.info("Initializing PlayerRepository");
    }

    @Override
    public Player findOne(Integer integer) {
        logger.traceEntry("Finding Player with id {}", integer);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Player.class, integer);
        }
    }

    @Override
    public Iterable<Player> findAll() {
        logger.traceEntry("Finding all Players");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Player ", Player.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Players", e);
            return null;
        }
    }

    @Override
    public void save(Player entity) {
        logger.traceEntry("Saving Player: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        }
        logger.info("Player saved: {}", entity);
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Player with id {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Player p = session.get(Player.class, integer);
            if (p != null)
                session.remove(p);
            tx.commit();
        }
    }

    @Override
    public void update(Player entity) {
        logger.traceEntry("Updating Player: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        }
    }
}

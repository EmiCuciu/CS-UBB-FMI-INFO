package com.persistence.repo;

import com.model.Joc;
import com.model.Player;
import com.persistence.IJocRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class JocRepository implements IJocRepository {

    private static final Logger logger = LogManager.getLogger();


    public JocRepository() {
        logger.info("Initializing JocRepository");
    }

    @Override
    public Iterable<Joc> findAllByPlayer(Player player) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Joc WHERE player.porecla = :alias";
            return session.createQuery(hql, Joc.class)
                    .setParameter("alias", player.getPorecla())
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error finding games for player: " + player, e);
            return null;
        }
    }

    @Override
    public Joc findOne(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Joc.class, integer);
        } catch (Exception e) {
            logger.error("Error finding game with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Joc> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Joc", Joc.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all games: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Joc entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        }
    }

    @Override
    public void delete(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Joc joc = session.find(Joc.class, integer);
            if (joc != null) {
                session.remove(joc);
                session.getTransaction().commit();
            } else {
                logger.warn("Game with id {} not found for deletion", integer);
            }
        } catch (Exception e) {
            logger.error("Error deleting game with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting game: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Joc entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error updating game {}: {}", entity, e.getMessage());
            throw new RuntimeException("Error updating game: " + e.getMessage(), e);
        }
    }
}

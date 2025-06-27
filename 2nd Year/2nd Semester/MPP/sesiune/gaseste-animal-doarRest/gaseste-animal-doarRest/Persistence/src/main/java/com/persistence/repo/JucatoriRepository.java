package com.persistence.repo;

import com.model.Jucator;
import com.persistence.IRepoJucatori;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JucatoriRepository implements IRepoJucatori {

    Logger logger = LogManager.getLogger();

    public JucatoriRepository() {
        logger.info("JucatoriRepository initialized");
    }

    @Override
    public Optional<Jucator> findByPorecla(String porecla) {
        logger.traceEntry("Finding Jucator by porecla: {}", porecla);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Jucator jucator = session.createQuery("from Jucator where porecla = :porecla", Jucator.class)
                    .setParameter("porecla", porecla)
                    .uniqueResult();
            return Optional.ofNullable(jucator);
        } catch (Exception e) {
            logger.error("Error finding Jucator by porecla: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Jucator findOne(Integer integer) {
        logger.traceEntry("Finding Jucator with id: {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Jucator.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Jucator with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Jucator> findAll() {
        logger.traceEntry("Finding all Jucatori");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Jucator", Jucator.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Jucatori: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Jucator entity) {
        logger.traceEntry("Saving Jucator: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving Jucator: {}", e.getMessage());
            throw new RuntimeException("Error saving Jucator: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Jucator with id: {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Jucator jucator = session.get(Jucator.class, integer);
            if (jucator != null) {
                session.remove(jucator);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting Jucator with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting Jucator: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Jucator entity) {
        logger.traceEntry("Updating Jucator: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error updating Jucator: {}", e.getMessage());
            throw new RuntimeException("Error updating Jucator: " + e.getMessage(), e);
        }
    }
}

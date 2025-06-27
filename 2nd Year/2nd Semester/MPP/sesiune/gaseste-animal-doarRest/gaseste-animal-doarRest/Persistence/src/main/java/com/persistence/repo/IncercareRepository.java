package com.persistence.repo;

import com.model.Incercare;
import com.persistence.IRepoIncercari;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class IncercareRepository implements IRepoIncercari {

    private final static Logger logger = LogManager.getLogger();

    public IncercareRepository() {
        logger.info("IncercareRepository initialized");
    }

    @Override
    public Incercare findOne(Integer integer) {
        logger.traceEntry("Finding Incercare with id {}", integer);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Incercare.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Incercare with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Incercare> findAll() {
        logger.traceEntry("Finding all Incercari");
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Incercare", Incercare.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Incercari: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Incercare entity) {
        logger.traceEntry("Saving Incercare: {}", entity);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving Incercare: {}", e.getMessage());
            throw new RuntimeException("Error saving Incercare: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Incercare with id {}", integer);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Incercare incercare = session.get(Incercare.class, integer);
            if (incercare != null) {
                session.remove(incercare);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting Incercare with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting Incercare: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Incercare entity) {
        logger.traceEntry("Updating Incercare: {}", entity);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Incercare incercare = session.get(Incercare.class, entity.getId());
            if (incercare != null) {
                session.merge(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error updating Incercare: {}", e.getMessage());
            throw new RuntimeException("Error updating Incercare: " + e.getMessage(), e);
        }
    }
}

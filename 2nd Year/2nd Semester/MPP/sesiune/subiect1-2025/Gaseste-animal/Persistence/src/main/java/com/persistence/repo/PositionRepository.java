package com.persistence.repo;

import com.model.Position;
import com.persistence.IPositionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;


public class PositionRepository implements IPositionRepository {

    private static final Logger logger = LogManager.getLogger();

    public PositionRepository() {
        logger.info("PositionRepository initialized");
    }

    @Override
    public Position findOne(Integer integer) {
        logger.traceEntry("Finding Position with id {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Position.class, integer);
        }
    }

    @Override
    public Iterable<Position> findAll() {
        logger.traceEntry("Finding all Positions");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Position", Position.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Positions: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Position entity) {
        logger.traceEntry("Saving Position: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving Position: {}", e.getMessage());
            throw new RuntimeException("Error saving Position: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Position with id {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Position position = session.get(Position.class, integer);
            if (position != null) {
                session.remove(position);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting Position with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting Position: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Position entity) {
        logger.traceEntry("Updating Position: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error updating Position: {}", e.getMessage());
            throw new RuntimeException("Error updating Position: " + e.getMessage(), e);
        }
    }
}

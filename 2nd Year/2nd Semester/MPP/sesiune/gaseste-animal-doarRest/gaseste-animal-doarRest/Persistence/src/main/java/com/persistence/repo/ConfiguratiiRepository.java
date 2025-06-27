package com.persistence.repo;

import com.model.Configuratie;
import com.persistence.IRepoConfiguratii;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;


@Repository
public class ConfiguratiiRepository implements IRepoConfiguratii {

    private static final Logger logger = LogManager.getLogger();

    public ConfiguratiiRepository() {
        logger.info("ConfiguratiiRepository initialized");
    }

    @Override
    public Configuratie findOne(Integer integer) {
        logger.traceEntry("Finding Configuratie with id {}", integer);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Configuratie.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Configuratie with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Configuratie> findAll() {
        logger.traceEntry("Finding all Configuratii");
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuratie", Configuratie.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Configuratii: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Configuratie entity) {
        logger.traceEntry("Saving Configuratie: {}", entity);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving Configuratie: {}", e.getMessage());
            throw new RuntimeException("Error saving Configuratie: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Configuratie with id {}", integer);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Configuratie configuratie = session.get(Configuratie.class, integer);
            if (configuratie != null) {
                session.remove(configuratie);
            }
            session.getTransaction().commit();
            logger.info("Configuratie with id {} deleted successfully", integer);
        } catch (Exception e) {
            logger.error("Error deleting Configuratie with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting Configuratie: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Configuratie entity) {
        logger.traceEntry("Updating Configuratie: {}", entity);
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Configuratie configuratie = session.get(Configuratie.class, entity.getId());
            if (configuratie != null) {
                session.merge(entity);
                session.getTransaction().commit();
                logger.info("Configuratie with id {} updated successfully", entity.getId());
            } else {
                logger.warn("Configuratie with id {} not found for update", entity.getId());
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            logger.error("Error updating Configuratie: {}", e.getMessage());
            throw new RuntimeException("Error updating Configuratie: " + e.getMessage(), e);
        }
    }
}

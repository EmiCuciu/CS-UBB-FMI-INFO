package com.persistence.repo;

import com.model.Configuratie;
import com.persistence.IConfiguratieRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ConfiguratieRepository implements IConfiguratieRepository {

    private static final Logger logger = LogManager.getLogger();

    public ConfiguratieRepository() {
        logger.info("ConfiguratieRepository initialized");
    }

    @Override
    public Configuratie findOne(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Configuratie.class, integer);
        }
        catch (Exception e) {
            logger.error("Error finding Configuratie with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Configuratie> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuratie", Configuratie.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Configuraties: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Configuratie entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Configuratie configuratie = session.get(Configuratie.class, integer);
            if (configuratie != null) {
                session.remove(configuratie);
            } else {
                logger.warn("Configuratie with id {} not found for deletion", integer);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting Configuratie with id {}: {}", integer, e.getMessage());
        }
    }

    @Override
    public void update(Configuratie entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error updating Configuratie {}: {}", entity, e.getMessage());
        }
    }
}

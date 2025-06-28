package persistence;

import domain.Configuratie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoConfiguratii;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoConfiguratiiHibernate implements IRepoConfiguratii {
    private Logger logger = LogManager.getLogger();

    @Override
    public void save(Configuratie entity) {
        logger.info("Saving Configuratie with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error saving Configuratie: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.info("Deleting Configuratie with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Configuratie configuratie = session.find(Configuratie.class, integer);
            if (configuratie != null) {
                session.remove(configuratie);
                tx.commit();
            } else {
                logger.warn("Configuratie with id {} not found", integer);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting Configuratie: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Configuratie> findOne(Integer integer) {
        logger.info("Finding Configuratie with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Configuratie.class, integer));
        } catch (Exception e) {
            logger.error("Error finding Configuratie: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Configuratie> findAll() {
        logger.info("Finding all Configuratii");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Configuratie", Configuratie.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Configuratii: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Configuratie entity) {
        logger.info("Updating Configuratie: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error updating Configuratie: {}", e.getMessage());
        }
    }
}
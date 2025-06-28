package persistence;

import domain.Incercare;
import domain.Joc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoIncercari;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoIncercariHibernate implements IRepoIncercari {
    private Logger logger = LogManager.getLogger();

    @Override
    public void save(Incercare entity) {
        logger.info("Saving Incercare with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error saving Incercare: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.info("Deleting Incercare with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Incercare incercare = session.find(Incercare.class, integer);
            if (incercare != null) {
                session.remove(incercare);
                tx.commit();
            } else {
                logger.warn("Incercare with id {} not found", integer);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting Incercare: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Incercare> findOne(Integer integer) {
        logger.info("Finding Incercare with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Incercare.class, integer));
        } catch (Exception e) {
            logger.error("Error finding Incercare: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Incercare> findAll() {
        logger.info("Finding all Incercari");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Incercare", Incercare.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Incercari: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Incercare entity) {
        logger.info("Updating Incercare: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error updating Incercare: {}", e.getMessage());
        }
    }

    @Override
    public List<Incercare> findByJoc(Joc joc) {
        logger.info("Finding Incercari by Joc: {}", joc.getId());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Incercare where joc = :joc", Incercare.class)
                    .setParameter("joc", joc)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding Incercari by Joc: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public long countByJoc(Joc joc) {
        logger.info("Counting Incercari by Joc: {}", joc.getId());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select count(*) from Incercare where joc = :joc", Long.class)
                    .setParameter("joc", joc)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting Incercari by Joc: {}", e.getMessage());
            return 0;
        }
    }
}
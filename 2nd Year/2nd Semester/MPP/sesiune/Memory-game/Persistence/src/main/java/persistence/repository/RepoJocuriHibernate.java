package persistence.repository;

import domain.Joc;
import domain.Jucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoJocuri;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoJocuriHibernate implements IRepoJocuri {
    private Logger logger = LogManager.getLogger();

    @Override
    public void save(Joc entity) {
        logger.info("Saving Joc with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error saving Joc: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.info("Deleting Joc with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Joc joc = session.find(Joc.class, integer);
            if (joc != null) {
                session.remove(joc);
                tx.commit();
            } else {
                logger.warn("Joc with id {} not found", integer);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting Joc: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Joc> findOne(Integer id) {
        logger.info("Finding Joc with id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Joc joc = session.createQuery(
                            "SELECT j FROM Joc j " +
                                    "JOIN FETCH j.configuratie c " +
                                    "JOIN FETCH j.jucator " +
                                    "LEFT JOIN FETCH c.cuvinte " +
                                    "WHERE j.id = :id", Joc.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(joc);
        } catch (Exception e) {
            logger.error("Error finding Joc: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Joc> findAll() {
        logger.info("Finding all Jocuri");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Joc", Joc.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Jocuri: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Joc entity) {
        logger.info("Updating Joc: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error updating Joc: {}", e.getMessage());
        }
    }

    @Override
    public List<Joc> findByJucator(Jucator jucator) {
        logger.info("Finding Jocuri by Jucator: {}", jucator.getnume());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Joc where jucator = :jucator", Joc.class)
                    .setParameter("jucator", jucator)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding Jocuri by Jucator: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Joc> findAllFinishedGames() {
        logger.info("Finding all finished Jocuri");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Joc where status = 'FINALIZAT' order by punctaj, dataSfarsit - dataInceput", Joc.class)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding finished Jocuri: {}", e.getMessage());
            return List.of();
        }
    }
}
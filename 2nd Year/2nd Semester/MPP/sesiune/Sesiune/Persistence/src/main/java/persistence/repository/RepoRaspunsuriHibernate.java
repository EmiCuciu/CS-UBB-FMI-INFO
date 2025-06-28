package persistence.repository;

import domain.Intrebare;
import domain.Joc;
import domain.RaspunsJucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoRaspunsuri;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoRaspunsuriHibernate implements IRepoRaspunsuri {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void save(RaspunsJucator entity) {
        logger.info("Saving RaspunsJucator with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("RaspunsJucator saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving RaspunsJucator: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        logger.info("Deleting RaspunsJucator with id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            RaspunsJucator raspuns = session.find(RaspunsJucator.class, id);
            if (raspuns != null) {
                session.remove(raspuns);
                tx.commit();
                logger.info("RaspunsJucator with id {} deleted successfully", id);
            } else {
                logger.warn("RaspunsJucator with id {} not found", id);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting RaspunsJucator with id {}: {}", id, e.getMessage());
        }
    }

    @Override
    public Optional<RaspunsJucator> findOne(Integer id) {
        logger.info("Finding RaspunsJucator with id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            RaspunsJucator raspuns = session.find(RaspunsJucator.class, id);
            return Optional.ofNullable(raspuns);
        } catch (Exception e) {
            logger.error("Error finding RaspunsJucator with id {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<RaspunsJucator> findAll() {
        logger.info("Finding all RaspunsJucator");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RaspunsJucator", RaspunsJucator.class).list();
        } catch (Exception e) {
            logger.error("Error finding all RaspunsJucator: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(RaspunsJucator entity) {
        logger.info("Updating RaspunsJucator: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            logger.info("RaspunsJucator updated successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error updating RaspunsJucator: {}", e.getMessage());
        }
    }

    @Override
    public List<RaspunsJucator> findByJoc(Joc joc) {
        logger.info("Finding RaspunsJucator by Joc: {}", joc);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from RaspunsJucator where joc = :joc", RaspunsJucator.class)
                    .setParameter("joc", joc)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding RaspunsJucator by Joc {}: {}", joc, e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<RaspunsJucator> findByJocAndIntrebare(Joc joc, Intrebare intrebare) {
        logger.info("Finding RaspunsJucator by Joc and Intrebare: {} {}", joc, intrebare);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from RaspunsJucator where joc = :joc and intrebare = :intrebare", 
                    RaspunsJucator.class)
                    .setParameter("joc", joc)
                    .setParameter("intrebare", intrebare)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding RaspunsJucator by Joc and Intrebare: {}", e.getMessage());
            return List.of();
        }
    }
}
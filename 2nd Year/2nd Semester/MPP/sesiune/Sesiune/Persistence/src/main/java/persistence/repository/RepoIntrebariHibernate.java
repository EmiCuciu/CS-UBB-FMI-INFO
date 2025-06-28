package persistence.repository;

import domain.Intrebare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoIntrebari;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoIntrebariHibernate implements IRepoIntrebari {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void save(Intrebare entity) {
        logger.info("Saving Intrebare with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Intrebare saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving Intrebare: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        logger.info("Deleting Intrebare with id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Intrebare intrebare = session.find(Intrebare.class, id);
            if (intrebare != null) {
                session.remove(intrebare);
                tx.commit();
                logger.info("Intrebare with id {} deleted successfully", id);
            } else {
                logger.warn("Intrebare with id {} not found", id);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting Intrebare with id {}: {}", id, e.getMessage());
        }
    }

    @Override
    public Optional<Intrebare> findOne(Integer id) {
        logger.info("Finding Intrebare with id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Intrebare intrebare = session.find(Intrebare.class, id);
            return Optional.ofNullable(intrebare);
        } catch (Exception e) {
            logger.error("Error finding Intrebare with id {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Intrebare> findAll() {
        logger.info("Finding all Intrebari");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Intrebare", Intrebare.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Intrebari: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Intrebare entity) {
        logger.info("Updating Intrebare: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            logger.info("Intrebare updated successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error updating Intrebare: {}", e.getMessage());
        }
    }

    @Override
    public List<Intrebare> findByNivel(int nivel) {
        logger.info("Finding Intrebari by nivel: {}", nivel);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Intrebare where nivel = :nivel", Intrebare.class)
                    .setParameter("nivel", nivel)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding Intrebari by nivel {}: {}", nivel, e.getMessage());
            return List.of();
        }
    }
}
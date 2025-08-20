package persistence.repository;

import domain.Cuvant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoCuvinte;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
public class RepoCuvinteHibernate implements IRepoCuvinte {
    private final Logger logger = LogManager.getLogger();

    @Override
    public void save(Cuvant entity) {
        logger.info("Saving Cuvant with Hibernate: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error saving Cuvant: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.info("Deleting Cuvant with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Cuvant cuvant = session.find(Cuvant.class, integer);
            if (cuvant != null) {
                session.remove(cuvant);
                tx.commit();
            } else {
                logger.warn("Cuvant with id {} not found", integer);
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting Cuvant: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Cuvant> findOne(Integer integer) {
        logger.info("Finding Cuvant with id: {}", integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Cuvant.class, integer));
        } catch (Exception e) {
            logger.error("Error finding Cuvant: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Cuvant> findAll() {
        logger.info("Finding all Cuvinte");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Cuvant", Cuvant.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Cuvinte: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Cuvant entity) {
        logger.info("Updating Cuvant: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error updating Cuvant: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Cuvant> findByText(String text) {
        logger.info("Finding Cuvant by text: {}", text);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Cuvant cuvant = session.createQuery(
                            "from Cuvant where text = :text", Cuvant.class)
                    .setParameter("text", text)
                    .uniqueResult();
            return Optional.ofNullable(cuvant);
        } catch (Exception e) {
            logger.error("Error finding Cuvant by text: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Cuvant> getRandomCuvinte(int count) {
        logger.info("Getting {} random Cuvinte", count);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Cuvant order by random()", Cuvant.class)
                    .setMaxResults(count)
                    .list();
        } catch (Exception e) {
            logger.error("Error getting random Cuvinte: {}", e.getMessage());
            return List.of();
        }
    }
}
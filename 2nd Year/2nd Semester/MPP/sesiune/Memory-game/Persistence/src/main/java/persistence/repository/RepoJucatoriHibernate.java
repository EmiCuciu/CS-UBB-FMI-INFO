package persistence.repository;

import domain.Jucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import persistence.repository.IRepositories.IRepoJucatori;
import persistence.repository.utils.JdbcUtils;
import persistence.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;


@Repository
public class RepoJucatoriHibernate implements IRepoJucatori {
    private Logger logger = LogManager.getLogger();
    @Override
    public void save(Jucator entity) {
        logger.info("Saving Jucator with Hibernate: " + entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error("Error saving Jucator with Hibernate: " + entity);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.info("Deleting Jucator with id with Hibernate: " + integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Jucator Jucator = session.find(Jucator.class, integer);
            if(Jucator != null){
                session.remove(Jucator);
                tx.commit();
                logger.info("Jucator with id " + integer + " deleted successfully with Hibernate");
            } else {
                logger.warn("Jucator with id " + integer + " not found with Hibernate");
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error saving Jucator with Hibernate: " + integer);
        }
    }

    @Override
    public Optional<Jucator> findOne(Integer integer) {
        logger.info("Finding Jucator with Hibernate: " + integer);
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.find(Jucator.class, integer));
        } catch (Exception e) {
            logger.error("Error finding Jucator with id {} with Hibernate", integer);
            return Optional.empty();
        }
    }

    @Override
    public List<Jucator> findAll() {
        logger.info("Finding all Configuratii with Hibernate");
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("from Jucator").list(); // aici e numele clasei, nu numele tabelei din baza de date!
        } catch (Exception e) {
            logger.error("Error finding all Configuratii with Hibernate");
            return List.of();
        }
    }

    @Override
    public void update(Jucator entity) {
        logger.info("Updating Jucator with Hibernate: " + entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Jucator Jucator = session.find(Jucator.class, entity.getId());
            if(Jucator != null){
                session.merge(entity);
                tx.commit();
            }
            else {
                tx.rollback();
            }
        } catch (Exception e) {
            logger.error("Error updating Jucator with Hibernate: " + entity);
        }
    }

    @Override
    public Optional<Jucator> findByNume(String nume) {
        logger.info("Finding Jucator by nume with Hibernate: {}", nume);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Jucator jucator = session.createQuery(
                            "from Jucator where nume = :nume", Jucator.class)
                    .setParameter("nume", nume)
                    .uniqueResult();
            if (jucator != null) {
                logger.info("Found Jucator with nume {}: {}", nume, jucator);
            } else {
                logger.warn("No Jucator found with nume: {}", nume);
            }
            return Optional.ofNullable(jucator);
        } catch (Exception e) {
            logger.error("Error finding Jucator by nume: {}", nume, e);
            return Optional.empty();
        }
    }

}

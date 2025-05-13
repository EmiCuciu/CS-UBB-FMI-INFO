package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Medicament;
import com.example.laboratoriss.Repository.IMedicamentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateMedicamentRepository implements IMedicamentRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateMedicamentRepository(Properties properties) {
        logger.info("Initializing HibernateMedicamentRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory(properties);
    }

    @Override
    public Medicament findOne(Integer id) {
        logger.traceEntry("finding Medicament with id {}", id);

        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Medicament WHERE id = :id", Medicament.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding Medicament with id {}: {}", id, e.getMessage());
        }
        logger.traceExit("found Medicament with id {}", id);
        return null;
    }

    @Override
    public Iterable<Medicament> findAll() {
        logger.traceEntry("finding all Medicaments");
        List<Medicament> medicaments = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            medicaments = session.createQuery("FROM Medicament", Medicament.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Medicaments: {}", e.getMessage());
        }
        logger.traceExit("found {} Medicaments", medicaments.size());
        return medicaments;
    }

    @Override
    public void save(Medicament entity) {
        logger.traceEntry("saving Medicament {}", entity);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error saving Medicament {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("saved Medicament {}", entity);
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting Medicament with id {}", id);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Medicament medicament = session.find(Medicament.class, id);
                if (medicament != null) {
                    session.remove(medicament);
                    tx.commit();
                } else {
                    logger.warn("Medicament with id {} not found", id);
                }
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error deleting Medicament with id {}: {}", id, e.getMessage());
            }
        }
        logger.traceExit("deleted Medicament with id {}", id);
    }

    @Override
    public void update(Medicament entity) {
        logger.traceEntry("updating Medicament {}", entity);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error updating Medicament {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("updated Medicament {}", entity);
    }
}
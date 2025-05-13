package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.ComandaItem;
import com.example.laboratoriss.Repository.IComandaItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HibernateComandaItemRepository implements IComandaItemRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public HibernateComandaItemRepository(Properties properties) {
        logger.info("Initializing HibernateComandaItemRepository with properties: {}", properties);
        sessionFactory = HibernateUtil.getSessionFactory(properties);
    }

    @Override
    public ComandaItem findOne(Integer id) {
        logger.traceEntry("finding ComandaItem with id {}", id);

        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM ComandaItem WHERE id = :id", ComandaItem.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            logger.error("Error finding ComandaItem with id {}: {}", id, e.getMessage());
        }
        logger.traceExit("found ComandaItem with id {}", id);
        return null;
    }

    @Override
    public Iterable<ComandaItem> findAll() {
        logger.traceEntry("finding all ComandaItems");
        List<ComandaItem> comandaItems = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            comandaItems = session.createQuery("FROM ComandaItem", ComandaItem.class).list();
        } catch (Exception e) {
            logger.error("Error finding all ComandaItems: {}", e.getMessage());
        }
        logger.traceExit("found {} ComandaItems", comandaItems.size());
        return comandaItems;
    }

    @Override
    public Iterable<ComandaItem> findByComandaId(Integer comandaId) {
        logger.traceEntry("finding ComandaItems for comanda with id {}", comandaId);
        List<ComandaItem> comandaItems = new ArrayList<>();

        try (var session = sessionFactory.openSession()) {
            // We need to join with Comanda table to filter by comandaId
            comandaItems = session.createQuery(
                            "SELECT ci FROM ComandaItem ci JOIN Comanda c ON c.id = :comandaId", ComandaItem.class)
                    .setParameter("comandaId", comandaId)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding ComandaItems for comanda with id {}: {}", comandaId, e.getMessage());
        }
        logger.traceExit("found {} ComandaItems for comanda with id {}" + comandaItems.size() + comandaId);
        return comandaItems;
    }

    @Override
    public void save(ComandaItem entity) {
        logger.traceEntry("saving ComandaItem {}", entity);

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
                logger.error("Error saving ComandaItem {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("saved ComandaItem {}", entity);
    }

    public void saveForComanda(ComandaItem item, int comandaId) {
        logger.traceEntry("saving ComandaItem {} for comanda with id {}", item, comandaId);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                // Set the comanda reference
                var comanda = session.find(Comanda.class, comandaId);
                if (comanda != null) {
                    // Set the comanda for the item
                    // Note: The ComandaItem class might need a setComanda method
                    session.persist(item);
                    tx.commit();
                } else {
                    logger.warn("Comanda with id {} not found", comandaId);
                }
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error saving ComandaItem {} for comanda with id {}: {}",
                        item, comandaId, e.getMessage());
            }
        }
        logger.traceExit("saved ComandaItem {} for comanda with id {}" + item + comandaId);
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting ComandaItem with id {}", id);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                ComandaItem comandaItem = session.find(ComandaItem.class, id);
                if (comandaItem != null) {
                    session.remove(comandaItem);
                    tx.commit();
                } else {
                    logger.warn("ComandaItem with id {} not found", id);
                }
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error deleting ComandaItem with id {}: {}", id, e.getMessage());
            }
        }
        logger.traceExit("deleted ComandaItem with id {}", id);
    }

    public void deleteByComandaId(Integer comandaId) {
        logger.traceEntry("deleting ComandaItems for comanda with id {}", comandaId);

        try (var session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                int deletedCount = session.createNativeQuery(
                                "DELETE FROM ComandaItem WHERE comanda_id = :comandaId")
                        .setParameter("comandaId", comandaId)
                        .executeUpdate();
                tx.commit();
                logger.info("Deleted {} ComandaItems for comanda with id {}", deletedCount, comandaId);
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Error deleting ComandaItems for comanda with id {}: {}",
                        comandaId, e.getMessage());
            }
        }
        logger.traceExit("deleted ComandaItems for comanda with id {}", comandaId);
    }

    @Override
    public void update(ComandaItem entity) {
        logger.traceEntry("updating ComandaItem {}", entity);

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
                logger.error("Error updating ComandaItem {}: {}", entity, e.getMessage());
            }
        }
        logger.traceExit("updated ComandaItem {}", entity);
    }
}
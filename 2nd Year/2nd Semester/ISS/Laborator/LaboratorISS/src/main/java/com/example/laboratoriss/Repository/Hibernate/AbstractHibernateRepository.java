package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Entity;
import com.example.laboratoriss.Repository.IRepository;
import com.example.laboratoriss.Utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractHibernateRepository<ID, E extends Entity<ID>> implements IRepository<ID, E> {

    private static final Logger logger = LogManager.getLogger();
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractHibernateRepository() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    @Override
    public E findOne(ID id) {
        logger.traceEntry("finding entity with id: {}", id);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            E entity = session.get(entityClass, (Serializable) id);
            return entity;
        }
    }

    @Override
    public Iterable<E> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaQuery<E> query = session.getCriteriaBuilder().createQuery(entityClass);
            query.from(entityClass);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public void save(E entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving entity", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(E entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating entity", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            E entity = session.get(entityClass, (Serializable) id);
            if (entity != null) {
                session.delete(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting entity", e);
            throw new RuntimeException(e);
        }
    }
}
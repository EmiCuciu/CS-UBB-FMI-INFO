package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IComandaRepository;
import com.example.laboratoriss.Utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ComandaRepository extends AbstractHibernateRepository<Integer, Comanda>
        implements IComandaRepository {

    private static final Logger logger = LogManager.getLogger();

    public ComandaRepository() {
        logger.info("Initializing ComandaRepository with Hibernate");
    }

    @Override
    public List<Comanda> findByUser(User user) {
        logger.traceEntry("finding commands for user: {}", user.getId());
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Comanda> query = session.createQuery(
                    "from Comanda c where c.user.id = :userId",
                    Comanda.class);
            query.setParameter("userId", user.getId());
            return query.getResultList();
        }
    }

    @Override
    public List<Comanda> findByStatus(Status status) {
        logger.traceEntry("finding commands with status: {}", status);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Comanda> query = session.createQuery(
                    "from Comanda c where c.status = :status",
                    Comanda.class);
            query.setParameter("status", status);
            return query.getResultList();
        }
    }
}
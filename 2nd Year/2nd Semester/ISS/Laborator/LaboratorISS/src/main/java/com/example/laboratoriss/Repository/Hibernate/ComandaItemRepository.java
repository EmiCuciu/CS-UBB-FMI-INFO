package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.ComandaItem;
import com.example.laboratoriss.Repository.IComandaItemRepository;
import com.example.laboratoriss.Utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ComandaItemRepository extends AbstractHibernateRepository<Integer, ComandaItem>
        implements IComandaItemRepository {

    private static final Logger logger = LogManager.getLogger();

    public ComandaItemRepository() {
        logger.info("Initializing ComandaItemRepository with Hibernate");
    }

    @Override
    public Iterable<ComandaItem> findByComandaId(Integer comandaId) {
        logger.traceEntry("finding command items for command id: {}", comandaId);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<ComandaItem> query = session.createQuery(
                    "from ComandaItem ci where ci.id = :comandaId",
                    ComandaItem.class);
            query.setParameter("comandaId", comandaId);
            return query.getResultList();
        }
    }
}
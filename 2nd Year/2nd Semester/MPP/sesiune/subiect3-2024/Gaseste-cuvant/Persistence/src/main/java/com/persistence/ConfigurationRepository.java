package com.persistence;

import com.model.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationRepository implements IConfigurationRepository{

    private static final Logger logger = LogManager.getLogger(ConfigurationRepository.class);

    public ConfigurationRepository () {
        logger.info("ConfigurationRepository initialized");
    }

    @Override
    public Configuration findOne(Integer id) {
        final AtomicReference<Configuration> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Configuration configuration = session.get(Configuration.class, id);
            result.set(configuration);
        });

        return result.get();
    }

    @Override
    public Iterable<Configuration> findAll() {
        AtomicReference<List<Configuration>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Configuration> query = session.createQuery("from Configuration", Configuration.class);
            result.set(query.getResultList());
        });

        return result.get();
    }

    @Override
    public void save(Configuration entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
        });
    }

    @Override
    public void delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Configuration configuration = session.get(Configuration.class, id);
            if (configuration != null) {
                session.remove(configuration);
            }
        });
    }

    @Override
    public void update(Configuration entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    public Configuration getRandomConfiguration() {
        AtomicReference<List<Configuration>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Configuration> query = session.createQuery("from Configuration", Configuration.class);
            result.set(query.getResultList());
        });

        List<Configuration> configs = result.get();
        if (configs != null && !configs.isEmpty()) {
            Random random = new Random();
            return configs.get(random.nextInt(configs.size()));
        }
        return null;
    }
}
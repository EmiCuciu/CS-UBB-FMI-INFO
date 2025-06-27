package com.persistence;

import com.model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerRepository implements IPlayerRepository {

    private static final Logger logger = LogManager.getLogger(PlayerRepository.class);

    public PlayerRepository() {
        logger.info("PlayerRepository initialized");
    }

    @Override
    public Player findOne(Integer id) {
        AtomicReference<Player> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Player player = session.get(Player.class, id);
            result.set(player);
        });

        return result.get();
    }

    @Override
    public Iterable<Player> findAll() {
        AtomicReference<List<Player>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Player> query = session.createQuery("from Player", Player.class);
            result.set(query.getResultList());
        });

        return result.get();
    }

    @Override
    public void save(Player entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
        });
    }

    @Override
    public void delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Player player = session.get(Player.class, id);
            if (player != null) {
                session.remove(player);
            }
        });
    }

    @Override
    public void update(Player entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    public Player findByAlias(String alias) {
        AtomicReference<Player> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Player> query = session.createQuery(
                    "from Player where alias = :alias", Player.class);
            query.setParameter("alias", alias);
            List<Player> players = query.getResultList();
            if (!players.isEmpty()) {
                result.set(players.get(0));
            }
        });

        return result.get();
    }
}
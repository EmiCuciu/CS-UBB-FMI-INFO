package com.persistence;

import com.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GameRepository implements IGameRepository {

    private static final Logger logger = LogManager.getLogger(GameRepository.class);

    public GameRepository() {
        logger.info("GameRepository initialized");
    }

    @Override
    public Game findOne(Integer id) {
        AtomicReference<Game> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Game game = session.get(Game.class, id);
            result.set(game);
        });

        return result.get();
    }

    @Override
    public Iterable<Game> findAll() {
        AtomicReference<List<Game>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Game> query = session.createQuery("from Game", Game.class);
            result.set(query.getResultList());
        });

        return result.get();
    }

    @Override
    public void save(Game entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
        });
    }

    @Override
    public void delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Game game = session.get(Game.class, id);
            if (game != null) {
                session.remove(game);
            }
        });
    }

    @Override
    public void update(Game entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    public List<Game> getGamesByPlayerWithMinCorrectWords(String playerAlias, int minCorrectWords) {
        AtomicReference<List<Game>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Game> query = session.createQuery(
                    "from Game g where g.player.alias = :alias and g.nrOfCorrectWords >= :minWords",
                    Game.class);
            query.setParameter("alias", playerAlias);
            query.setParameter("minWords", minCorrectWords);
            result.set(query.getResultList());
        });

        return result.get();
    }

    public List<Game> getAllGamesForRanking() {
        AtomicReference<List<Game>> result = new AtomicReference<>();

        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Query<Game> query = session.createQuery(
                    "from Game g order by g.score desc", Game.class);
            result.set(query.getResultList());
        });

        return result.get();
    }
}
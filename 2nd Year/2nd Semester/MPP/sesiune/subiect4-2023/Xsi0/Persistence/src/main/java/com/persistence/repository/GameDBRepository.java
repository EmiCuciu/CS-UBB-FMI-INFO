package com.persistence.repository;

import com.model.Game;
import com.model.Player;
import com.persistence.IGameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GameDBRepository implements IGameRepository {

    private static final Logger logger = LogManager.getLogger();

    public GameDBRepository() {
        logger.info("Initializing GameDBRepository");
    }

    @Override
    public Iterable<Game> findAllByPlayer(Player player) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Game WHERE player.alias = :alias";
            return session.createQuery(hql, Game.class)
                    .setParameter("alias", player.getAlias())
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error finding games for player: " + player, e);
            return null;
        }
    }

    @Override
    public Optional<Game> findOne(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Game WHERE id=:idM", Game.class)
                    .setParameter("idM", id)
                    .uniqueResult());
        } catch (Exception e) {
            logger.error("Error finding game with id: " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Game> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Game ", Game.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Games", e);
            return null;
        }
    }

    @Override
    public Optional<Game> save(Game entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return Optional.of(entity);
    }

    @Override
    public Optional<Game> delete(Long aLong) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Game game = session.find(Game.class, aLong);
            if (game != null) {
                session.beginTransaction();
                session.remove(game);
                session.getTransaction().commit();
                return Optional.of(game);
            } else {
                logger.warn("Game with id {} not found for deletion", aLong);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error deleting game with id: " + aLong, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Game> update(Game entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Game existingGame = session.find(Game.class, entity.getId());
            if (existingGame != null) {
                session.beginTransaction();
                existingGame.setPlayer(entity.getPlayer());
                existingGame.setScore(entity.getScore());
                session.update(existingGame);
                session.getTransaction().commit();
                return Optional.of(existingGame);
            } else {
                logger.warn("Game with id {} not found for update", entity.getId());
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error updating game: " + entity, e);
            return Optional.empty();
        }
    }
}

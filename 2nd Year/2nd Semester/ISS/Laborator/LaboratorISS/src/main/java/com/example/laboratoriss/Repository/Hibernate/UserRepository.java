package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IUserRepository;
import com.example.laboratoriss.Utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepository extends AbstractHibernateRepository<Integer, User>
        implements IUserRepository {

    private static final Logger logger = LogManager.getLogger();

    public UserRepository() {
        logger.info("Initializing UserRepository with Hibernate");
    }

    public User findByUsername(String username) {
        logger.traceEntry("finding user by username: {}", username);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        }
    }

    public User findByUsernameAndPassword(String username, String password) {
        logger.traceEntry("finding user by username and password");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username and password = :password",
                    User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            List<User> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        }
    }
}
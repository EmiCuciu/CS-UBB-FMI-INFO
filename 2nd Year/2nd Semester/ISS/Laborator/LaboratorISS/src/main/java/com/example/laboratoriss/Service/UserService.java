package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService extends AbstractService<User> implements IUserService {

    private final IUserRepository repository;
    private static final Logger logger = LogManager.getLogger();

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User login(String username, String password) {
        logger.traceEntry("login attempt with username: {}", username);

        for (User user : repository.findAll()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                logger.traceExit("login successful for: {}", username);
                return user;
            }
        }

        logger.traceExit("login failed for: {}", username);
        return null;
    }

    @Override
    public User register(User user) {
        logger.traceEntry("registering user: {}", user);

        // Check if username already exists
        for (User existingUser : repository.findAll()) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                logger.traceExit("username already exists: {}", user.getUsername());
                return null;
            }
        }

        repository.save(user);
        logger.traceExit("registered user: {}", user);
        notifyAdd(user);
        return user;
    }

    @Override
    public Iterable<User> getAll() {
        logger.traceEntry("getting all users");
        return repository.findAll();
    }

    @Override
    public User findOne(Integer id) {
        logger.traceEntry("finding user with id: {}", id);
        return repository.findOne(id);
    }

    @Override
    public void save(User user) {
        logger.traceEntry("saving user: {}", user);
        repository.save(user);
        notifyAdd(user);
        logger.traceExit();
    }

    @Override
    public void update(User user) {
        logger.traceEntry("updating user: {}", user);
        repository.update(user);
        notifyUpdate(user);
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting user with id: {}", id);
        User user = repository.findOne(id);
        if (user != null) {
            repository.delete(id);
            notifyDelete(user);
        }
        logger.traceExit();
    }
}
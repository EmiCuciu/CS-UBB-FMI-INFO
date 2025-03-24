package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Utils.Observer.Observable;

public interface IUserService extends Observable<User> {
    User login(String username, String password);

    User register(User user);

    Iterable<User> getAll();

    User findOne(Integer id);

    void save(User user);

    void update(User user);

    void delete(Integer id);
}
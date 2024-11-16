package org.example.lab6.service;



import org.example.lab6.Domain.User;
import org.example.lab6.repository.Repository;
import org.example.lab6.utils.events.ChangeEventType;
import org.example.lab6.utils.events.UserEntityChangeEvent;
import org.example.lab6.utils.observer.Observable;
import org.example.lab6.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements Observable<UserEntityChangeEvent> {
    private Repository<Long, User> repo;
    private List<Observer<UserEntityChangeEvent>> observers=new ArrayList<>();

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }


    public User addUtilizator(User user) {
        if(repo.save(user).isEmpty()){
            UserEntityChangeEvent event = new UserEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public User deleteUtilizator(Long id){
        Optional<User> user=repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();
        }
        return null;
    }

    public Iterable<User> getAll(){
        return repo.findAll();
    }



    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

    public User updateUser(User u) {
        Optional<User> oldUser=repo.findOne(u.getId());
        if(oldUser.isPresent()) {
            Optional<User> newUser=repo.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UserEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }


}

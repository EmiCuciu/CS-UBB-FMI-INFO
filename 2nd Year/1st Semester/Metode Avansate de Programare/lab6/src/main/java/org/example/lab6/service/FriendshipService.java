package org.example.lab6.service;

import org.example.lab6.Utils.events.ChangeEventType;
import org.example.lab6.Utils.events.FriendshipEntityChangeEvent;
import org.example.lab6.Utils.observer.Observable;
import org.example.lab6.Utils.observer.Observer;
import org.example.lab6.domain.Friendship;
import org.example.lab6.domain.Tuple;
import org.example.lab6.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


public class FriendshipService implements EntityService<Tuple<Long,Long>, Friendship>,
        Observable<FriendshipEntityChangeEvent> {

    private static FriendshipService instance = new FriendshipService();
    private final UserService userService = UserService.getInstance();
    private Repository<Tuple<Long,Long>,Friendship> repo;

    private final List<Observer<FriendshipEntityChangeEvent>> observers = new ArrayList<>();

    private FriendshipService() {}
    public static synchronized FriendshipService getInstance() {
        if(instance == null) {
            instance = new FriendshipService();
        }
        return instance;
    }

    public void setRepo(Repository<Tuple<Long, Long>, Friendship> repo) {
        this.repo = repo;
    }

    public void deletedUser(Long id){
        List<Friendship> friendships = new ArrayList<>((Collection<Friendship>) getAll());
        friendships.forEach(friendship -> {
            if(friendship.getId().getE1().equals(id) || friendship.getId().getE2().equals(id)){
                delete(friendship.getId());
            }
        });

    }
    public List<Friendship> getUserFriends(Long id){
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(friendship -> friendship.getId().getE1().equals(id) || friendship.getId().getE2().equals(id))
                .toList();
    }
    @Override
    public Repository<Tuple<Long, Long>, Friendship> getRepo() {
        return repo;
    }

    @Override
    public Optional<Friendship> create(Friendship entity) {
        if(EntityService.super.create(entity).isPresent()){
            FriendshipEntityChangeEvent event = new FriendshipEntityChangeEvent(ChangeEventType.ADD,entity);
            notifyObservers(event);
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        Optional<Friendship> oldFriendship = repo.findOne(entity.getId());
        if(oldFriendship.isPresent()){
            Optional<Friendship> newFriendship = repo.update(entity);
            if(newFriendship.isPresent()){
                notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.UPDATE,entity,oldFriendship.get()));
            }
            return newFriendship;
        }
        return oldFriendship;
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> entityId) {
        Optional<Friendship> friendship = repo.delete(entityId);
        if(friendship.isPresent()){
            notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.DELETE,friendship.get()));

        }
        return friendship;
    }

    @Override
    public Iterable<Friendship> getAll() {
        return EntityService.super.getAll();
    }

    @Override
    public Optional<Friendship> getById(Tuple<Long, Long> entityId) {
        return EntityService.super.getById(entityId);
    }

    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipEntityChangeEvent t) {
        observers.forEach(x-> x.update(t));
    }
}

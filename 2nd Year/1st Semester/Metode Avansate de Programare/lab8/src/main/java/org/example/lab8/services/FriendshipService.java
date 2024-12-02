package org.example.lab8.services;

import org.example.lab8.domain.Prietenie;
import org.example.lab8.domain.Utilizator;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.utils.events.ChangeEventType;
import org.example.lab8.utils.events.FriendshipEvent;
import org.example.lab8.utils.observer.Observable;
import org.example.lab8.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendshipService implements Observable<FriendshipEvent> {
    private final PrietenieDBRepository prietenieDBRepository;
    private final List<Observer<FriendshipEvent>> observers = new ArrayList<>();

    public FriendshipService(PrietenieDBRepository prietenieDBRepository) {
        this.prietenieDBRepository = prietenieDBRepository;
    }

    public void addFriendRequest(Long userId, Long friendId) {
        prietenieDBRepository.addFriendRequest(userId, friendId);
        FriendshipEvent event = new FriendshipEvent(ChangeEventType.REQUESTED, new Prietenie(userId, friendId));
        notifyObservers(event);
    }

    public void acceptFriendRequest(Long userId, Long friendId) {
        prietenieDBRepository.acceptFriendRequest(userId, friendId);
        FriendshipEvent event = new FriendshipEvent(ChangeEventType.ACCEPTED, new Prietenie(userId, friendId));
        notifyObservers(event);
    }

    public void removeFriend(Long userId, Long friendId) {
        prietenieDBRepository.removeFriend(userId, friendId);
        FriendshipEvent event = new FriendshipEvent(ChangeEventType.DELETE, new Prietenie(userId, friendId));
        notifyObservers(event);
    }

    public Set<Prietenie> findFriendRequests(Long userId) {
        return prietenieDBRepository.findFriendRequests(userId);
    }

    public Set<Utilizator> findFriends(Long userId) {
        return prietenieDBRepository.findFriends(userId);
    }

    @Override
    public void addObserver(Observer<FriendshipEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipEvent t) {
        observers.forEach(o -> o.update(t));
    }
}
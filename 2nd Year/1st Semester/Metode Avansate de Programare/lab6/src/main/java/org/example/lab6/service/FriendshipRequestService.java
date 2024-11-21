package org.example.lab6.service;




import org.example.lab6.Utils.events.ChangeEventType;
import org.example.lab6.Utils.events.FriendshipRequestEntityChangeEvent;
import org.example.lab6.Utils.observer.Observable;
import org.example.lab6.Utils.observer.Observer;
import org.example.lab6.domain.FriendshipRequest;
import org.example.lab6.domain.Tuple;
import org.example.lab6.domain.User;
import org.example.lab6.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestService implements Observable<FriendshipRequestEntityChangeEvent> {
    private final Repository<Tuple<Long, Long>, FriendshipRequest> requestRepository;
    private final FriendshipService friendshipService;
    private final List<Observer<FriendshipRequestEntityChangeEvent>> observers = new ArrayList<>();

    public FriendshipRequestService(Repository<Tuple<Long, Long>, FriendshipRequest> requestRepository, FriendshipService friendshipService) {
        this.requestRepository = requestRepository;
        this.friendshipService = friendshipService;
    }

    public Iterable<FriendshipRequest> getRequestsForUser(User user) {
        List<FriendshipRequest> userRequests = new ArrayList<>();
        for (FriendshipRequest request : requestRepository.findAll())
            if (request.getId().getE2().equals(user.getId()))
                userRequests.add(request);
        return userRequests;
    }

    public void addRequest(User sender, User receiver) {
        FriendshipRequest newRequest = new FriendshipRequest(
                sender.getId(),
                receiver.getId(),
                LocalDateTime.now(),
                "pending"
        );
        if (sender.getId().equals(receiver.getId()))
            throw new ServiceException("You can't send a friend request to yourself!");
        if (friendshipService.getById(new Tuple<>(sender.getId(), receiver.getId())).isPresent() ||
                friendshipService.getById(new Tuple<>(receiver.getId(), sender.getId())).isPresent())
            throw new ServiceException("You can't send a friend request to a friend!");
        if(requestRepository.findOne(new Tuple<>(sender.getId(), receiver.getId())).isPresent()  || requestRepository.findOne(new Tuple<>(receiver.getId(), sender.getId())).isPresent()){
            throw new ServiceException("You can't send a friend request twice!");
        }
        requestRepository.save(newRequest);
        notifyObservers(new FriendshipRequestEntityChangeEvent(ChangeEventType.ADD, newRequest));
    }

    public void removeRequest(Long senderID, Long receiverID) {
        requestRepository.delete(new Tuple<>(senderID, receiverID));
    }

    @Override
    public void addObserver(Observer<FriendshipRequestEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipRequestEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipRequestEntityChangeEvent t) {
        observers.forEach(observer -> observer.update(t));
    }
}

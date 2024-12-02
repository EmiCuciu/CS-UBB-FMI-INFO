package org.example.lab8.services;

import org.example.lab8.domain.Message;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.utils.events.ChangeEventType;
import org.example.lab8.utils.events.MessageEvent;
import org.example.lab8.utils.observer.Observable;
import org.example.lab8.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageService implements Observable<MessageEvent> {
    private final MessageDBRepository messageDBRepository;
    private final List<Observer<MessageEvent>> observers = new ArrayList<>();

    public MessageService(MessageDBRepository messageDBRepository) {
        this.messageDBRepository = messageDBRepository;
    }

    public void sendMessage(Message message) {
        messageDBRepository.save(message);
        notifyObservers(new MessageEvent(ChangeEventType.SEND, message));
    }

    public List<Message> getMessages(Long userId1, Long userId2) {
        return messageDBRepository.getMessages(userId1, userId2);
    }

    public Optional<Message> getMessage(Long id) {
        return messageDBRepository.findOne(id);
    }

    @Override
    public void addObserver(Observer<MessageEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageEvent t) {
        observers.forEach(o -> o.update(t));
    }
}
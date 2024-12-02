package org.example.lab8.services;


import org.example.lab8.domain.validators.UtilizatorValidator;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.repository.dbrepo.UserDBRepository;

public class Service {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;

    public Service(UserDBRepository userDBRepository, PrietenieDBRepository prietenieDBRepository, MessageDBRepository messageDBRepository) {
        this.userService = new UserService(userDBRepository, new UtilizatorValidator());
        this.friendshipService = new FriendshipService(prietenieDBRepository);
        this.messageService = new MessageService(messageDBRepository);
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public MessageService getMessageService() {
        return messageService;
    }


}
package org.example.lab8.services;


import org.example.lab8.domain.PrietenieValidator;
import org.example.lab8.domain.UtilizatorValidator;
import org.example.lab8.repository.dbrepo.MessageDBRepository;
import org.example.lab8.repository.dbrepo.PrietenieDBRepository;
import org.example.lab8.repository.dbrepo.UserDBRepository;

public class Service {
    UtilizatorValidator validator = new UtilizatorValidator();
    PrietenieValidator prietenieValidator = new PrietenieValidator();

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;

    public Service(UserDBRepository userDBRepository, PrietenieDBRepository prietenieDBRepository, MessageDBRepository messageDBRepository) {
        this.userService = new UserService(userDBRepository);
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
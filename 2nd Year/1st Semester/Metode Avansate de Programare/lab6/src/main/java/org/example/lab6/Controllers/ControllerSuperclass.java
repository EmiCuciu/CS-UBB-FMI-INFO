package org.example.lab6.Controllers;


import org.example.lab6.service.FriendshipRequestService;
import org.example.lab6.service.FriendshipService;
import org.example.lab6.service.ScreenService;
import org.example.lab6.service.UserService;

public class ControllerSuperclass implements Controller{
    private UserService userService;
    private ScreenService screenService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;

    public FriendshipRequestService getFriendshipRequestService() {
        return friendshipRequestService;
    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
    }

    public ScreenService getScreenService() {
        return screenService;
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setScreenService(ScreenService sceneService) {
        this.screenService = sceneService;
    }


    public void init() {

    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}

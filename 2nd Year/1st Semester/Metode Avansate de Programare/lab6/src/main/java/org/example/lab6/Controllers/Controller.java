package org.example.lab6.Controllers;


import org.example.lab6.service.FriendshipService;
import org.example.lab6.service.ScreenService;
import org.example.lab6.service.UserService;

public interface Controller {
    void setUserService(UserService userService);
    void setScreenService(ScreenService screenService);
    void setFriendshipService(FriendshipService friendshipService);
    void init();
}

package chat.network.jsonprotocol;


import chat.network.dto.MessageDTO;
import chat.network.dto.UserDTO;

import java.util.Arrays;

public class Request {
    private RequestType type;
    private UserDTO user;
    private MessageDTO message;
    private UserDTO[] friends;

    public Request(){}
    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO messageDTO) {
        this.message = messageDTO;
    }

    public UserDTO[] getFriends() {
        return friends;
    }

    public void setFriends(UserDTO[] friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", user=" + user +
                ", messageDTO=" + message +
                ", friends=" + Arrays.toString(friends) +
                '}';
    }
}

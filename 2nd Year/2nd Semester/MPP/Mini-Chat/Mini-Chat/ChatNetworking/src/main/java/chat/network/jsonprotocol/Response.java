package chat.network.jsonprotocol;

import chat.network.dto.MessageDTO;
import chat.network.dto.UserDTO;

import java.io.Serializable;
import java.util.Arrays;


public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private UserDTO user;
    private UserDTO[] friends;
    private MessageDTO message;

    public Response() {
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO[] getFriends() {
        return friends;
    }

    public void setFriends(UserDTO[] friends) {
        this.friends = friends;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                ", friends=" + Arrays.toString(friends) +
                ", message=" + message +
                '}';
    }
}

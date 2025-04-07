package chat.network.jsonprotocol;

import chat.model.Message;
import chat.model.User;
import chat.network.dto.DTOUtils;


public class JsonProtocolUtils {
    public static Response createNewMessageResponse(Message message){
        Response resp=new Response();
        resp.setType(ResponseType.NEW_MESSAGE);
        resp.setMessage(DTOUtils.getDTO(message));
        return resp;
    }

    public static Response createFriendLoggedInResponse(User friend){
        Response resp=new Response();
        resp.setType(ResponseType.FRIEND_LOGGED_IN);
        resp.setUser(DTOUtils.getDTO(friend));
        return resp;
    }

    public static Response createFriendLoggedOutResponse(User friend){
        Response resp=new Response();
        resp.setType(ResponseType.FRIEND_LOGGED_OUT);
        resp.setUser(DTOUtils.getDTO(friend));
        return resp;
    }

    public static Response createOkResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage){
        Response resp=new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Response createGetLoggedFriendsResponse(User[] friends){
        Response resp=new Response();
        resp.setType(ResponseType.GET_LOGGED_FRIENDS);
        resp.setFriends(DTOUtils.getDTO(friends));
        return resp;
    }

    public static Request createLoginRequest(User user){
        Request req=new Request();
        req.setType(RequestType.LOGIN);
        req.setUser(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createSendMessageRequest(Message message){
        Request req=new Request();
        req.setType(RequestType.SEND_MESSAGE);
        req.setMessage(DTOUtils.getDTO(message));
        return req;
    }

    public static Request createLogoutRequest(User user){
        Request req=new Request();
        req.setType(RequestType.LOGOUT);
        req.setUser(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createLoggedFriendsRequest(User user){
        Request req=new Request();
        req.setType(RequestType.GET_LOGGED_FRIENDS);
        req.setUser(DTOUtils.getDTO(user));
        return req;
    }
}

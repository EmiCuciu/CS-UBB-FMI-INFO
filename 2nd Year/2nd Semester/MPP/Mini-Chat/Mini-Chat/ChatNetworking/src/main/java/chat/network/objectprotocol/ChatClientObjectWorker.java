package chat.network.objectprotocol;

import chat.model.Message;
import chat.model.User;
import chat.network.dto.DTOUtils;
import chat.network.dto.MessageDTO;
import chat.network.dto.UserDTO;
import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ChatClientObjectWorker implements Runnable, IChatObserver {
    private IChatServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ChatClientObjectWorker.class);

    public ChatClientObjectWorker(IChatServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException|ClassNotFoundException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    public void messageReceived(Message message) throws ChatException {
        MessageDTO mdto= DTOUtils.getDTO(message);
        logger.debug("Message received  "+message);
        try {
            sendResponse(new NewMessageResponse(mdto));
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    public void friendLoggedIn(User friend) throws ChatException {
        UserDTO udto= DTOUtils.getDTO(friend);
        logger.debug("Friend logged in "+friend);
        try {
            sendResponse(new FriendLoggedInResponse(udto));
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void friendLoggedOut(User friend) throws ChatException {
        UserDTO udto= DTOUtils.getDTO(friend);
        logger.debug("Friend logged out "+friend);
        try {
            sendResponse(new FriendLoggedOutResponse(udto));
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LoginRequest){
            logger.debug("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            UserDTO udto=logReq.getUser();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return new OkResponse();
            } catch (ChatException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            logger.debug("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            UserDTO udto=logReq.getUser();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return new OkResponse();

            } catch (ChatException e) {
               return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof SendMessageRequest){
            logger.debug("SendMessageRequest ...");
            SendMessageRequest senReq=(SendMessageRequest)request;
            MessageDTO mdto=senReq.getMessage();
            Message message= DTOUtils.getFromDTO(mdto);
            try {
                server.sendMessage(message);
                 return new OkResponse();
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof GetLoggedFriendsRequest){
            logger.debug("GetLoggedFriends Request ...");
            GetLoggedFriendsRequest getReq=(GetLoggedFriendsRequest)request;
            UserDTO udto=getReq.getUser();
            User user= DTOUtils.getFromDTO(udto);
            try {
                User[] friends=server.getLoggedFriends(user);
                UserDTO[] frDTO= DTOUtils.getDTO(friends);
                return new GetLoggedFriendsResponse(frDTO);
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        logger.debug("sending response {}",response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}

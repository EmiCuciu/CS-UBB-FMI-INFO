package chat.network.rpcprotocol;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;


public class ChatClientRpcReflectionWorker implements Runnable, IChatObserver {
    private IChatServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ChatClientRpcReflectionWorker.class);

    public ChatClientRpcReflectionWorker(IChatServices server, Socket connection) {
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
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
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
        Response resp=new Response.Builder().type(ResponseType.NEW_MESSAGE).data(mdto).build();
        logger.debug("Message received  "+message);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    public void friendLoggedIn(User friend) throws ChatException {
        UserDTO udto=DTOUtils.getDTO(friend);
        Response resp=new Response.Builder().type(ResponseType.FRIEND_LOGGED_IN).data(udto).build();
        logger.debug("Friend logged in "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void friendLoggedOut(User friend) throws ChatException {
        UserDTO udto=DTOUtils.getDTO(friend);
        Response resp=new Response.Builder().type(ResponseType.FRIEND_LOGGED_OUT).data(udto).build();
        logger.debug("Friend logged out "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        logger.debug("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            logger.debug("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

        return response;
    }

    private Response handleLOGIN(Request request){
        logger.debug("Login request ..."+request.type());
        UserDTO udto=(UserDTO)request.data();
        User user=DTOUtils.getFromDTO(udto);
        try {
            server.login(user, this);
            return okResponse;
        } catch (ChatException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        logger.debug("Logout request...");
        UserDTO udto=(UserDTO)request.data();
        User user=DTOUtils.getFromDTO(udto);
        try {
            server.logout(user, this);
            connected=false;
            return okResponse;

        } catch (ChatException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleSEND_MESSAGE(Request request){
            logger.debug("SendMessageRequest ...");
            MessageDTO mdto=(MessageDTO)request.data();
            Message message=DTOUtils.getFromDTO(mdto);
            try {
                server.sendMessage(message);
                return okResponse;
            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }


    }

    private Response handleGET_LOGGED_FRIENDS(Request request){
        logger.debug("GetLoggedFriends Request ...");
        UserDTO udto=(UserDTO)request.data();
        User user=DTOUtils.getFromDTO(udto);
        try {
            User[] friends=server.getLoggedFriends(user);
            UserDTO[] frDTO=DTOUtils.getDTO(friends);
            return new Response.Builder().type(ResponseType.GET_LOGGED_FRIENDS).data(frDTO).build();
        } catch (ChatException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException{
        logger.debug("sending response {}",response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}

package chat.network.jsonprotocol;

import chat.model.Message;
import chat.model.User;
import chat.network.dto.DTOUtils;
import chat.network.dto.UserDTO;
import chat.network.utils.TextUtils;
import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServicesJsonProxy implements IChatServices {
    private String host;
    private int port;

    private IChatObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    private static Logger logger = LogManager.getLogger(ChatServicesJsonProxy.class);

    public ChatServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public void login(User user, IChatObserver client) throws ChatException {
        initializeConnection();
        user.setPasswd(TextUtils.simpleEncode(user.getPasswd()));
        Request req= JsonProtocolUtils.createLoginRequest(user);
        sendRequest(req);
         Response response=readResponse();
        if (response.getType()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            closeConnection();
            throw new ChatException(err);
        }
    }

    public void sendMessage(Message message) throws ChatException {

        Request req=JsonProtocolUtils.createSendMessageRequest(message);
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new ChatException(err);
        }
    }

    public void logout(User user, IChatObserver client) throws ChatException {

        Request req=JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
       Response response=readResponse();
        closeConnection();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new ChatException(err);
        }
    }



    public User[] getLoggedFriends(User user) throws ChatException {

        Request req=JsonProtocolUtils.createLoggedFriendsRequest(user);
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new ChatException(err);
        }
        UserDTO[] frDTO=response.getFriends();
        User[] friends=DTOUtils.getFromDTO(frDTO);
        return friends;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

    }

    private void sendRequest(Request request)throws ChatException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ChatException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ChatException {
       Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }
    private void initializeConnection() throws ChatException {
        try {
            gsonFormatter=new Gson();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished=false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response){
        if (response.getType()== ResponseType.FRIEND_LOGGED_IN){

            User friend=DTOUtils.getFromDTO(response.getUser());
            logger.debug("Friend logged in "+friend);
            try {
                client.friendLoggedIn(friend);
            } catch (ChatException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        if (response.getType()== ResponseType.FRIEND_LOGGED_OUT){
            User friend=DTOUtils.getFromDTO(response.getUser());
            logger.debug("Friend logged out "+friend);
            try {
                client.friendLoggedOut(friend);
            } catch (ChatException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }

        if (response.getType()== ResponseType.NEW_MESSAGE){
            Message message=DTOUtils.getFromDTO(response.getMessage());
            logger.debug("New message received {}", message);
            try {
                client.messageReceived(message);
            } catch (ChatException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.getType()== ResponseType.FRIEND_LOGGED_OUT || response.getType()== ResponseType.FRIEND_LOGGED_IN || response.getType()== ResponseType.NEW_MESSAGE;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    logger.debug("response received {}",responseLine);
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }
}

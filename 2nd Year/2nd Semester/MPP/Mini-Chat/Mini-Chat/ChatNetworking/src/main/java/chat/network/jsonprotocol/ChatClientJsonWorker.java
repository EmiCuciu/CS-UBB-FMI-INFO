package chat.network.jsonprotocol;

import chat.model.Message;
import chat.model.User;
import chat.network.dto.DTOUtils;
import chat.network.dto.MessageDTO;
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

public class ChatClientJsonWorker implements Runnable, IChatObserver {
    private IChatServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ChatClientJsonWorker.class);

    public ChatClientJsonWorker(IChatServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter=new Gson();
        try{
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
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

        Response resp= JsonProtocolUtils.createNewMessageResponse(message);
        logger.debug("Message received  "+message);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    public void friendLoggedIn(User friend) throws ChatException {

        Response resp=JsonProtocolUtils.createFriendLoggedInResponse(friend);
        logger.debug("Friend logged in "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void friendLoggedOut(User friend) throws ChatException {

        Response resp=JsonProtocolUtils.createFriendLoggedOutResponse(friend);
        logger.debug("Friend logged out "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){
       Response response=null;
        if (request.getType()== RequestType.LOGIN){
            logger.debug("Login request ...{}"+request.getUser());
            UserDTO udto=request.getUser();
            User user=DTOUtils.getFromDTO(udto);
            user.setPasswd(TextUtils.simpleDecode(user.getPasswd()));
            try {
                server.login(user, this);
                return okResponse;
            } catch (ChatException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGOUT){
            logger.debug("Logout request {}",request.getUser());
            UserDTO udto=request.getUser();
            User user=DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ChatException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.SEND_MESSAGE){

            MessageDTO mdto=(MessageDTO)request.getMessage();
            Message message=DTOUtils.getFromDTO(mdto);
            logger.debug("SendMessageRequest ...{} ",message);
            try {
                server.sendMessage(message);
                return okResponse;
            } catch (ChatException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()== RequestType.GET_LOGGED_FRIENDS){
            logger.debug("GetLoggedFriends Request ...user= {}",request.getUser());
            UserDTO udto=request.getUser();
            User user=DTOUtils.getFromDTO(udto);
            try {
                User[] friends=server.getLoggedFriends(user);

                return JsonProtocolUtils.createGetLoggedFriendsResponse(friends);
            } catch (ChatException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        logger.debug("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}

package network.object_protocol;

import domain.Jucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IObserver;
import services.IServices;
import services.ServiceException;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private static Logger logger = LogManager.getLogger(ServicesProxy.class);
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses= new LinkedBlockingQueue<>();
    }

    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }
    }

    private Response readResponse() {
        Response response=null;
        while(true) {
            try{
                response=qresponses.take();
                if(response instanceof UpdateResponse){
                    handleUpdate((UpdateResponse)response);
                    continue;
                }
                break;
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }

        return response;
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

    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
            logger.debug("Connection initialized successfully!");
            System.out.println("Connection initialized successfully!");
        }   catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
            System.out.println("Error:" + e.getStackTrace());
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update) {

    }


    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.debug("response received {}",response);
                    if (response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse)response);
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException|ClassNotFoundException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }

    @Override
    public void logIn(Jucator jucator, IObserver observer) {
        initializeConnection();

        this.client = observer;

        sendRequest(new LogInRequest(jucator));

        Response r = readResponse();

        if(r instanceof OkResponse){
            return;
        }
        if(r instanceof ErrorResponse){
            ErrorResponse error = (ErrorResponse) r;
            closeConnection();
            throw new ServiceException(error.getMessage());
        }
    }

    @Override
    public void logOut(Jucator jucator, IObserver observer) {

    }
}

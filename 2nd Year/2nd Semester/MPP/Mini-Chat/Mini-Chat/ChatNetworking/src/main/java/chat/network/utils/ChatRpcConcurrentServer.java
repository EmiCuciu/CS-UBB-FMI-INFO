package chat.network.utils;

import chat.network.rpcprotocol.ChatClientRpcReflectionWorker;
import chat.services.IChatServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    private static Logger logger = LogManager.getLogger(ChatRpcConcurrentServer.class);
    public ChatRpcConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ChatClientRpcReflectionWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        logger.info("Stopping services ...");
    }
}

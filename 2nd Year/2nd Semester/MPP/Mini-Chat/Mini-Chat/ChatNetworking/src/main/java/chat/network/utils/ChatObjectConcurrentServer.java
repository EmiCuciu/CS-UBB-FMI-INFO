package chat.network.utils;


import chat.network.objectprotocol.ChatClientObjectWorker;
import chat.services.IChatServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;


public class ChatObjectConcurrentServer extends AbsConcurrentServer {

    private IChatServices chatServer;

    private static Logger logger = LogManager.getLogger(ChatJsonConcurrentServer.class);

    public ChatObjectConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("Chat-ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ChatClientObjectWorker worker=new ChatClientObjectWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}

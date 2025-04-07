package chat.network.utils;

import chat.network.jsonprotocol.ChatClientJsonWorker;
import chat.services.IChatServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class ChatJsonConcurrentServer extends AbsConcurrentServer{
    private IChatServices chatServer;
    private static Logger logger = LogManager.getLogger(ChatJsonConcurrentServer.class);
    public ChatJsonConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("Chat-ChatJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ChatClientJsonWorker worker=new ChatClientJsonWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}

package triatlon.network.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.network.jsonprotocol.TriatlonClientJsonWorker;
import triatlon.services.ITriatlonServices;

import java.net.Socket;

public class TriatlonJsonConcurrentServer extends AbsConcurrentServer{
    private ITriatlonServices chatServer;
    private static Logger logger = LogManager.getLogger(TriatlonJsonConcurrentServer.class);
    public TriatlonJsonConcurrentServer(int port, ITriatlonServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("Chat-ChatJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TriatlonClientJsonWorker worker=new TriatlonClientJsonWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}

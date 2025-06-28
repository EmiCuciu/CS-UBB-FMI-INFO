package network.utils;

import network.utils.AbstractConcurrentServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;
import network.object_protocol.ClientWorker;

import java.net.Socket;
import java.net.SocketException;

public class ConcurrentServer extends AbstractConcurrentServer {
    private IServices chatServer;
    private static Logger logger = LogManager.getLogger(ConcurrentServer.class);

    public ConcurrentServer(int port, IServices chatServer)
    {
        super(port);
        this.chatServer = chatServer;
        logger.info("Concurrent Server created!");
    }

    @Override
    protected Thread createWorker(Socket client) throws SocketException {
        ClientWorker worker = new ClientWorker(chatServer, client);
        return new Thread(worker);
    }
}

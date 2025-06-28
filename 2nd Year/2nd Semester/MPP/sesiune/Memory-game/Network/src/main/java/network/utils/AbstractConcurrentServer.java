package network.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractConcurrentServer extends AbstractServer{
    private static Logger logger = LogManager.getLogger(AbstractConcurrentServer.class);

    public AbstractConcurrentServer(int port)
    {
        super(port);
        logger.debug("Concurrent AbstractServer");
    }

    @Override
    protected void processRequest(Socket client) throws SocketException {
        Thread tw = createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client) throws SocketException;
}
package triatlon.network.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.network.protobuffprotocol.ProtoTriatlonWorker;
import triatlon.services.ITriatlonServices;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriatlonProtobuffConcurentServer extends AbsConcurrentServer {
    private ITriatlonServices triatlonServer;
    private static final Logger logger = LogManager.getLogger(TriatlonProtobuffConcurentServer.class);

    public TriatlonProtobuffConcurentServer(int port, ITriatlonServices triatlonServer) {
        super(port);
        this.triatlonServer = triatlonServer;
        logger.info("Initialized TriatlonProtobuffConcurentServer on port {}", port);
    }

    @Override
    protected Thread createWorker(Socket client) {
        logger.info("Creating worker for client {}", client.getInetAddress());
        ProtoTriatlonWorker worker = new ProtoTriatlonWorker(triatlonServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
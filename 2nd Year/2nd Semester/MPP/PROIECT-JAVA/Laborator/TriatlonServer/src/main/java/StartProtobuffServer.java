import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.network.utils.AbstractServer;
import triatlon.network.utils.ServerException;
import triatlon.network.utils.TriatlonProtobuffConcurentServer;

import triatlon.persistence.*;

import triatlon.server.TriatlonServicesImpl;

import triatlon.services.ITriatlonServices;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static final int DEFAULT_PORT = 55556;
    private static final Logger logger = LogManager.getLogger(StartProtobuffServer.class);

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartProtobuffServer.class.getResourceAsStream("/chatserver.properties"));
            logger.info("Server properties loaded: {}", serverProps);
        } catch (IOException e) {
            logger.error("Cannot find chatserver.properties: {}", e.getMessage());
            return;
        }

        IArbitruRepository arbitruRepository = new ArbitruRepository(serverProps);
        IParticipantRepository participantRepository = new ParticipantRepository(serverProps);
        IProbaRepository probaRepository = new ProbaRepository(serverProps, arbitruRepository);
        IRezultatRepository rezultatRepository = new RezultatRepository(serverProps, participantRepository, arbitruRepository);

        ITriatlonServices triatlonServices = new TriatlonServicesImpl(arbitruRepository, rezultatRepository, participantRepository, probaRepository);

        int serverPort = DEFAULT_PORT;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        } catch (NumberFormatException e) {
            logger.error("Invalid port number: {}", e.getMessage());
            logger.info("Using default port: {}", DEFAULT_PORT);
        }

        logger.info("Starting Protobuff server on port: {}", serverPort);
        AbstractServer server = new TriatlonProtobuffConcurentServer(serverPort, triatlonServices);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server: {}", e.getMessage());
        }
    }
}
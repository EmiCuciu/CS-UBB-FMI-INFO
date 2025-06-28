package server;

import network.utils.AbstractServer;
import network.utils.ConcurrentServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import persistence.repository.IRepositories.IRepoConfiguratii;
//import persistence.repository.IRepositories.IRepoIncercari;
//import persistence.repository.IRepositories.IRepoJocuri;
//import persistence.repository.IRepositories.IRepoJucatori;
//import persistence.repository.RepoConfiguratiiHibernate;
//import persistence.repository.RepoIncercariHibernate;
//import persistence.repository.RepoJocuriHibernate;
//import persistence.repository.RepoJucatoriHibernate;
//import services.IServices;

import java.io.File;
import java.io.IOException;
import network.utils.ServerException;
import persistence.repository.IRepositories.IRepoJucatori;
import persistence.repository.RepoJucatoriHibernate;
import services.IServices;

import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;
    private static Logger logger = LogManager.getLogger(StartServer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            if (StartServer.class.getClassLoader().getResourceAsStream("server.properties") == null) {
                logger.error("server.properties is NULL !!!");
            }

            props.load(StartServer.class.getClassLoader().getResourceAsStream("server.properties"));

            logger.info("Server properties set. {}", props);
            props.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find server.properties ", e);
            logger.debug("Looking into directory {}", (new File(".")).getAbsolutePath());
            return;
        }

        //IRepoConfiguratii configuratiiRepo = new RepoConfiguratiiHibernate();
        //IRepoJocuri jocuriRepo = new RepoJocuriHibernate();
        IRepoJucatori jucatoriRepo = new RepoJucatoriHibernate();
        //IRepoIncercari incercariRepo = new RepoIncercariHibernate();

        //IServices services = new ServiceImpl(configuratiiRepo, jocuriRepo, jucatoriRepo, incercariRepo);
        //asta e doar asa de proba...
        IServices services = new ServiceImpl(jucatoriRepo);


        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException e) {
            logger.error("Wrong port number" + e.getMessage());
            logger.debug("Using default port" + defaultPort);
        }
        logger.debug("Starting server on port: {}", serverPort);
        AbstractServer server = new ConcurrentServer(serverPort, services);
        try {
            server.start();
        } catch (ServerException e)
        {
            logger.error("Cannot start server" +  e);
        }
    }
}

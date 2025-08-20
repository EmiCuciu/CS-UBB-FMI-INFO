package rest;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"domain", "persistence", "services", "rest", "network.dto"})
public class StartRestServices {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("[INFO] Acum pornim aplicatia din rest...");
        //System.setProperty("log4j.debug", "true");
        SpringApplication.run(StartRestServices.class, args);
        logger.info("[INFO] A pornit!!!");
    }
}

/**
 * npx create-react-app memory-game
 * cd memory-game
 * npm install axios
 */
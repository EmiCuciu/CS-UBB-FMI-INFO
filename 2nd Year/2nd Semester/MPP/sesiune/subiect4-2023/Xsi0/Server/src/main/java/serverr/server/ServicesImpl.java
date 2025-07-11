package serverr.server;

import com.model.Game;
import com.model.Player;
import com.model.Position;
import com.persistence.IGameRepository;
import com.persistence.IPlayerRepository;
import com.persistence.IPositionRepository;
import com.services.AppException;
import com.services.IObserver;
import com.services.IServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;


public class ServicesImpl implements IServices {

    private static final Logger logger = LogManager.getLogger(ServicesImpl.class);

    private Map<String, IObserver> loggedClients;

    private IPlayerRepository playerRepository;
    private IGameRepository gameRepository;
    private IPositionRepository positionRepository;

    public ServicesImpl(IPlayerRepository playerRepository, IGameRepository gameRepository, IPositionRepository positionRepository) {
        logger.info("Initializing ServicesImpl");
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.positionRepository = positionRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Player login(Player player, IObserver client) throws AppException {
        logger.info("Player initialized: " + player);
        Player foundPlayer = findPlayerByAlias(player.getAlias());
        if (foundPlayer != null){
            if(loggedClients.get(player.getAlias()) != null)
                throw new AppException("Player already logged in.");
            loggedClients.put(player.getAlias(), client);
        } else
            throw new AppException("Authentication failed.");
        return player;
    }

    @Override
    public synchronized Game addGame(Game game) throws AppException {
        logger.info("Adding Game: " + game + " ...");
        Game addedGame = gameRepository.save(game).orElseThrow(() -> new AppException("Error adding the game."));
        // notify all clients that a new game was added
        for (IObserver client: loggedClients.values()) {
            try {
                client.gameAdded(addedGame);
            } catch (AppException e) {
                System.err.println("Error notifying client: " + e);
            }
        }
        return addedGame;
    }

    @Override
    public synchronized Position addPosition(Position position) throws AppException {
        logger.info("Adding Position: " + position + " ...");
        return positionRepository.save(position).orElseThrow(() -> new AppException("Error adding the position."));
    }

    @Override
    public synchronized Player findPlayerByAlias(String alias) {
        Iterable<Player> players = playerRepository.findAll();
        for (Player player : players) {
            if (player.getAlias().equals(alias)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Game> getAllGames() throws AppException {
        logger.info("Getting all games...");
        Iterable<Game> games = gameRepository.findAll();
        return StreamSupport.stream(games.spliterator(), false).toList();
    }

    @Override
    public synchronized Player logout(Player player, IObserver client) throws AppException {
        IObserver localClient = loggedClients.remove(player.getAlias());
        if (localClient == null)
            throw new AppException("Player " + player.getAlias() + " is not logged in.");
        return player;
    }
}

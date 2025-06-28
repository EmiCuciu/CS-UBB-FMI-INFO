package rest;

import network.dto.GameDetailsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest.GameRestService;
import services.ServiceException;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final Logger logger = LogManager.getLogger();

    private final GameRestService gameService;

    @Autowired
    public GameController(GameRestService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetailsDTO> getGameDetails(@PathVariable Integer id) {
        logger.info("REST request to get game details for ID: {}", id);
        try {
            GameDetailsDTO game = gameService.getGameDetails(id);
            return ResponseEntity.ok(game);
        } catch (ServiceException e) {
            logger.error("Error getting game details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error getting game details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
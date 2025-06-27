package com.rest.services;


import com.model.Game;
import com.model.Player;
import com.model.Position;
import com.persistence.IGameRepository;
import com.persistence.IPlayerRepository;
import com.persistence.IPositionRepository;
import com.persistence.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/xsi0")
public class Controller {

    private static final String template = "Hello, %s!";

    @Autowired
    private IGameRepository gameRepository;

    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    private IPositionRepository positionRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format(template, name);
    }


    @GetMapping("/game/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id){
        try {
            return gameRepository.findOne(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error getting game: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/positions/{game_id}")
    public List<Position> getPositionsByGame(@PathVariable Long game_id) {
        Game game = gameRepository.findOne(game_id).orElseThrow(() -> new RepositoryException("Game not found"));
        System.out.println("Game found: " + game);
        return StreamSupport.stream(positionRepository.findAllByGame(game).spliterator(), false).toList();
    }

    @GetMapping("/games/{alias}")
    public List<Game> getGamesByPlayer(@PathVariable String alias){
        Player foundPlayer = null;

        Iterable<Player> players = playerRepository.findAll();
        for(Player player : players) {
            if (player.getAlias().equals(alias)){
                foundPlayer = player;
                break;
            }
        }

        if (foundPlayer == null){
            throw new RepositoryException("Player not found");
        }

        return StreamSupport.stream(gameRepository.findAllByPlayer(foundPlayer).spliterator(),false)
                .sorted(Comparator.comparing(Game::getNoOfSeconds).reversed())
                .toList();
    }

    @PostMapping("/position")
    public ResponseEntity<Position> addPosition(@RequestBody Position position){
        Position savedPosition = positionRepository.save(position).orElseThrow(() -> new RepositoryException("Position could not be saved"));
        return new ResponseEntity<>(savedPosition, HttpStatus.CREATED);
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String useError(RepositoryException e){
        return e.getMessage();
    }
}

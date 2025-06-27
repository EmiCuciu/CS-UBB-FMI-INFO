package com.rest.services;

import com.model.Configuratie;
import com.model.Joc;
import com.model.Player;
import com.model.Position;
import com.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/gasesteAnimal")
public class Controller {

    private static final String template = "Hello, %s!";

    @Autowired
    private IJocRepository jocRepository;

    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    private IPositionRepository positionRepository;

    @Autowired
    private IConfiguratieRepository configuratieRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<Joc> getGameById(@PathVariable Integer id) {
        try {
            Joc joc = jocRepository.findOne(id);
            if (joc == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(joc, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/games/failed/{playerName}")
    public ResponseEntity<List<Joc>> getFailedGamesByPlayer(@PathVariable String playerName) {
        try {
            Player player = new Player();
            player.setPorecla(playerName);

            Iterable<Joc> games = jocRepository.findAllByPlayer(player);
            List<Joc> failedGames = StreamSupport.stream(games.spliterator(), false)
                    .filter(joc -> joc.getNrOfTries() == -1)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(failedGames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/games/{playerName}")
    public ResponseEntity<List<Joc>> getGamesByPlayer(@PathVariable String playerName) {
        try {
            Player player = new Player();
            player.setPorecla(playerName);

            Iterable<Joc> games = jocRepository.findAllByPlayer(player);
            List<Joc> gamesList = StreamSupport.stream(games.spliterator(), false)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(gamesList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/positions/{gameId}")
    public ResponseEntity<Position> getPositionsByGame(@PathVariable Integer gameId) {
        try {
            Joc joc = jocRepository.findOne(gameId);
            if (joc == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Get position from the configuration
            Configuratie config = joc.getConfiguratie();
            Integer positionId = config.getPozitie();
            Position position = positionRepository.findOne(positionId);

            if (position == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(position, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/configuration")
    public ResponseEntity<?> addConfiguration(@RequestBody Configuratie configuratie) {
        try {
            configuratieRepository.save(configuratie);
            return new ResponseEntity<>(configuratie, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/position")
    public ResponseEntity<Position> addPosition(@RequestBody Position position) {
        try {
            positionRepository.save(position);
            return new ResponseEntity<>(position, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/game")
    public ResponseEntity<Joc> addGame(@RequestBody Joc joc) {
        try {
            jocRepository.save(joc);
            return new ResponseEntity<>(joc, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers() {
        try {
            Iterable<Player> players = playerRepository.findAll();
            List<Player> playerList = StreamSupport.stream(players.spliterator(), false)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(playerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{alias}")
    public ResponseEntity<Player> getPlayerByAlias(@PathVariable String alias) {
        try {
            Iterable<Player> players = playerRepository.findAll();
            Player player = StreamSupport.stream(players.spliterator(), false)
                    .filter(p -> p.getPorecla().equals(alias))
                    .findFirst()
                    .orElse(null);

            if (player == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String useError(RepositoryException e) {
        return e.getMessage();
    }
}
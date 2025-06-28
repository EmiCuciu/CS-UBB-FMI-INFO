package com.rest;

import com.model.Joc;
import com.model.Incercare;
import com.model.Configuratie;
import com.network.dto.GameDetailsDTO;
import com.network.dto.PositionPairDTO;
import com.persistence.IRepoJocuri;
import com.persistence.IRepoIncercari;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/memory-game")
public class MemoryGameController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private IRepoJocuri jocuriRepository;

    @Autowired
    private IRepoIncercari incercariRepository;

    @GetMapping("/games/{gameId}")
    public ResponseEntity<?> getGameDetails(@PathVariable("gameId") int gameId) {
        logger.info("Getting game details for game ID: {}", gameId);

        // Find the game by ID
        Joc joc = jocuriRepository.findOne(gameId);
        if (joc == null) {
            logger.warn("Game with ID {} not found", gameId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        // Get player alias
        String playerAlias = joc.getJucator().getPorecla();

        // Get position pairs from attempts
        List<Incercare> incercari = new ArrayList<>();
        incercariRepository.findAll().forEach(incercari::add);

        // Filter attempts for this game
        List<Incercare> gameAttempts = incercari.stream()
                .filter(i -> i.getJoc().getId() == gameId)
                .collect(Collectors.toList());

        // Group attempts into position pairs
        List<PositionPairDTO> positionPairs = new ArrayList<>();
        for (int i = 0; i < gameAttempts.size(); i += 2) {
            if (i + 1 < gameAttempts.size()) {
                Incercare first = gameAttempts.get(i);
                Incercare second = gameAttempts.get(i + 1);
                positionPairs.add(new PositionPairDTO(
                        first.getLinie() * 5 + first.getColoana(),
                        second.getLinie() * 5 + second.getColoana()
                ));
            }
        }

        // Extract configuration
        // Note: For the memory game, we'll interpret the animal field as the word
        List<String> configuration = extractWordsConfiguration(joc);

        // Calculate score: -2 for matches, +3 for mismatches
        int totalScore = calculateGameScore(joc, configuration, positionPairs);

        // Calculate game duration in seconds
        long durationSeconds = 0;
        if (joc.getStartTime() != null) {
            Date endTime = new Date(); // Current time as end time if game is in progress
            durationSeconds = (endTime.getTime() - joc.getStartTime().getTime()) / 1000;
        }

        GameDetailsDTO gameDetails = new GameDetailsDTO(
                playerAlias,
                positionPairs,
                configuration,
                totalScore,
                durationSeconds
        );

        return ResponseEntity.ok(gameDetails);
    }

    private List<String> extractWordsConfiguration(Joc joc) {
        // In a real implementation, you would load the full configuration
        // This is a simplification based on current model
        List<String> config = new ArrayList<>(10);

        // We'll use the animal field as the word, and populate a 10-element list
        // For a complete implementation, you'd need to load all configuration entries for the game
        Configuratie conf = joc.getConfiguratie();
        if (conf != null && conf.getAnimal() != null) {
            // If animal field contains comma-separated words, parse them
            if (conf.getAnimal().contains(",")) {
                return Arrays.asList(conf.getAnimal().split(","));
            }

            // Otherwise create a demo configuration with 5 pairs
            for (int i = 0; i < 10; i++) {
                config.add(conf.getAnimal() + (i/2));
            }
        } else {
            // Fallback empty configuration
            for (int i = 0; i < 10; i++) {
                config.add("");
            }
        }

        return config;
    }

    private int calculateGameScore(Joc joc, List<String> configuration, List<PositionPairDTO> pairs) {
        int score = 0;

        // For each pair, check if the words match
        for (PositionPairDTO pair : pairs) {
            int pos1 = pair.getFirstPosition();
            int pos2 = pair.getSecondPosition();

            if (pos1 < configuration.size() && pos2 < configuration.size()) {
                if (configuration.get(pos1).equals(configuration.get(pos2))) {
                    // Matching pair: -2 points
                    score -= 2;
                } else {
                    // Non-matching pair: +3 points
                    score += 3;
                }
            }
        }

        return score;
    }
}
package com.rest;

import com.model.Configuratie;
import com.network.dto.ConfigurationUpdateDTO;
import com.persistence.IRepoConfiguratii;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/configurations")
public class MemoryGameConfigController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private IRepoConfiguratii configuratiiRepository;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConfiguration(
            @PathVariable("id") int configId,
            @RequestBody ConfigurationUpdateDTO updateDTO) {

        logger.info("Updating configuration with ID: {}", configId);

        // Verify configuration exists
        Configuratie configuratie = configuratiiRepository.findOne(configId);
        if (configuratie == null) {
            logger.warn("Configuration with ID {} not found", configId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Configuration not found");
        }

        // Validate the new configuration
        List<String> newConfig = updateDTO.getNewConfiguration();
        if (newConfig == null || newConfig.size() != 10) {
            logger.warn("Invalid configuration: must have exactly 10 items");
            return ResponseEntity.badRequest()
                    .body("Invalid configuration: must have exactly 10 items");
        }

        try {
            // Store the new configuration as a comma-separated string
            String configString = String.join(",", newConfig);
            configuratie.setAnimal(configString);

            // Update in database
            configuratiiRepository.update(configuratie);

            logger.info("Configuration updated successfully for ID: {}", configId);
            return ResponseEntity.ok("Configuration updated successfully");
        } catch (Exception e) {
            logger.error("Error updating configuration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating configuration: " + e.getMessage());
        }
    }

    // Optional: Get current configuration
    @GetMapping("/{id}")
    public ResponseEntity<?> getConfiguration(@PathVariable("id") int configId) {
        logger.info("Getting configuration with ID: {}", configId);

        Configuratie configuratie = configuratiiRepository.findOne(configId);
        if (configuratie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration not found");
        }

        // Parse configuration from the animal field
        List<String> wordConfiguration;
        if (configuratie.getAnimal() != null && configuratie.getAnimal().contains(",")) {
            wordConfiguration = List.of(configuratie.getAnimal().split(","));
        } else {
            wordConfiguration = List.of("a", "b", "a", "c", "b", "d", "d", "c", "e", "e");
        }

        return ResponseEntity.ok(wordConfiguration);
    }
}
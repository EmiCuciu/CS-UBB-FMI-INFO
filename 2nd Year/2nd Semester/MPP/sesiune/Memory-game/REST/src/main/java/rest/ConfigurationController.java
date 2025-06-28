package rest;

import network.dto.ConfigurationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest.GameRestService;
import services.ServiceException;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {
    private static final Logger logger = LogManager.getLogger();

    private final GameRestService gameService;

    @Autowired
    public ConfigurationController(GameRestService gameService) {
        this.gameService = gameService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfigurationDTO> updateConfiguration(
            @PathVariable Integer id,
            @RequestBody ConfigurationDTO configDto) {
        logger.info("REST request to update configuration ID: {}", id);
        try {
            ConfigurationDTO updated = gameService.updateConfiguration(id, configDto);
            return ResponseEntity.ok(updated);
        } catch (ServiceException e) {
            logger.error("Error updating configuration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error updating configuration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
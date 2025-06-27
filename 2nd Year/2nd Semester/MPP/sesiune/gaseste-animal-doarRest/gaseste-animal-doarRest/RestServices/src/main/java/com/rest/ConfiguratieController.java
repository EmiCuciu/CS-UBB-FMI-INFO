package com.rest;

import com.model.Configuratie;
import com.persistence.IRepoConfiguratii;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/configuratii")
public class ConfiguratieController {

    private static final Logger logger = LogManager.getLogger();

    private static final String template = "Hello, %s!";

    @Autowired
    private IRepoConfiguratii configuratiiRepository;

    public ConfiguratieController(IRepoConfiguratii configuratiiRepository) {
        this.configuratiiRepository = configuratiiRepository;
    }

    @RequestMapping("/greeting")
    public String greeting(String name) {
        logger.info("Greeting request received with name: {}", name);
        return String.format(template, name != null ? name : "World");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Configuratie configuratie){
        try {
            configuratiiRepository.save(configuratie);
            return ResponseEntity.status(201).body("Configuratie created with ID: " + configuratie.getId());
        } catch (Exception e) {
            System.err.println("Error creating configuratie: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating configuratie");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            logger.info("Retrieving all configuratii ");
            return ResponseEntity.ok(configuratiiRepository.findAll());
        } catch (Exception e) {
            logger.error("Error retrieving configuratii: {}", e.getMessage());
            System.err.println("Error retrieving configuratii: " + e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving configuratii");
        }
    }
}

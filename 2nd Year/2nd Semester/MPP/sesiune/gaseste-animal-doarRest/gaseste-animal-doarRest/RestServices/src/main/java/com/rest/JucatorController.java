package com.rest;


import com.model.Jucator;
import com.persistence.IRepoJucatori;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/jucatori")
public class JucatorController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private IRepoJucatori jucatoriRepository;

    public JucatorController(IRepoJucatori jucatoriRepository) {
        this.jucatoriRepository = jucatoriRepository;
    }

    private final String tempplate = "Hello, %s!";

    @RequestMapping("/greeting")
    public String greeting(String name) {
        return String.format(tempplate, name != null ? name : "World");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Jucator> getJucatorById(@PathVariable("id") int id) {
        logger.info("Getting Jucator with id: {}", id);
        Jucator jucator = jucatoriRepository.findOne(id);
        if (jucator == null) {
            logger.warn("Jucator with id {} not found", id);
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        logger.info("Found Jucator: {}", jucator);
        return ResponseEntity.ok(jucator); // 200 OK + jucator
    }

}

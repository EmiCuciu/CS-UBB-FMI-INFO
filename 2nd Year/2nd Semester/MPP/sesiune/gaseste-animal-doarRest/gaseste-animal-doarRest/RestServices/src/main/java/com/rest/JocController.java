package com.rest;

import com.network.dto.JocNeghicitDTO;
import com.persistence.IRepoJocuri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/jocuri")
public class JocController {

    private static final String template = "Hello, %s!";

    @Autowired
    private final IRepoJocuri jocuriRepository;

    public JocController(IRepoJocuri jocuriRepository) {
        this.jocuriRepository = jocuriRepository;
    }

    @RequestMapping("/greeting")
    public String greeting(String name) {
        return String.format(template, name != null ? name : "World");
    }


    @GetMapping("/neghicite/{id}")
    public ResponseEntity<List<JocNeghicitDTO>> getJocuriNeghicite(@PathVariable("id") int id){
        List<JocNeghicitDTO> jocuriNeghicite = jocuriRepository.findJocuriNeghicite(id);
        if (jocuriNeghicite.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(jocuriNeghicite); // 200 OK + lista
    }
}

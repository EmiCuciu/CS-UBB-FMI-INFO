// REST/src/main/java/rest/controllers/IntrebariController.java
package rest.controllers;

import domain.Intrebare;
import network.dto.IntrebareUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.repository.IRepositories.IRepoIntrebari;

import java.util.Optional;

@RestController
@RequestMapping("/api/intrebari")
public class IntrebariController {

    private final IRepoIntrebari intrebariRepository;

    @Autowired
    public IntrebariController(IRepoIntrebari intrebariRepository) {
        this.intrebariRepository = intrebariRepository;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIntrebare(
            @PathVariable("id") Integer id,
            @RequestBody IntrebareUpdateDTO updateDTO) {
        
        Optional<Intrebare> optionalIntrebare = intrebariRepository.findOne(id);
        if (optionalIntrebare.isEmpty()) {
            return new ResponseEntity<>("Intrebarea nu există", HttpStatus.NOT_FOUND);
        }
        
        Intrebare intrebare = optionalIntrebare.get();
        
        if (updateDTO.getText() != null && !updateDTO.getText().isEmpty()) {
            intrebare.setText(updateDTO.getText());
            intrebariRepository.update(intrebare);
            return new ResponseEntity<>(intrebare, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Textul întrebării nu poate fi gol", HttpStatus.BAD_REQUEST);
        }
    }
}
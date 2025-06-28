package rest.controllers;

import domain.Joc;
import domain.Jucator;
import domain.RaspunsJucator;
import network.dto.RaspunsIntrebareDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.repository.IRepositories.IRepoJocuri;
import persistence.repository.IRepositories.IRepoJucatori;
import persistence.repository.IRepositories.IRepoRaspunsuri;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/raspunsuri")
public class RaspunsuriController {

    private final IRepoRaspunsuri raspunsuriRepository;
    private final IRepoJucatori jucatoriRepository;
    private final IRepoJocuri jocuriRepository;

    @Autowired
    public RaspunsuriController(IRepoRaspunsuri raspunsuriRepository, 
                               IRepoJucatori jucatoriRepository,
                               IRepoJocuri jocuriRepository) {
        this.raspunsuriRepository = raspunsuriRepository;
        this.jucatoriRepository = jucatoriRepository;
        this.jocuriRepository = jocuriRepository;
    }

    @GetMapping("/{jucatorId}/{jocId}")
    public ResponseEntity<?> getRaspunsuriForJucatorAndJoc(
            @PathVariable("jucatorId") Integer jucatorId,
            @PathVariable("jocId") Integer jocId) {
        
        Optional<Jucator> optionalJucator = jucatoriRepository.findOne(jucatorId);
        if (optionalJucator.isEmpty()) {
            return new ResponseEntity<>("Jucatorul nu există", HttpStatus.NOT_FOUND);
        }
        
        Optional<Joc> optionalJoc = jocuriRepository.findOne(jocId);
        if (optionalJoc.isEmpty()) {
            return new ResponseEntity<>("Jocul nu există", HttpStatus.NOT_FOUND);
        }
        
        Joc joc = optionalJoc.get();
        
        if (!joc.getJucator().getId().equals(jucatorId)) {
            return new ResponseEntity<>("Jocul nu aparține acestui jucător", HttpStatus.FORBIDDEN);
        }
        
        List<RaspunsJucator> raspunsuri = raspunsuriRepository.findByJoc(joc);
        
        List<RaspunsIntrebareDTO> raspunsuriDTO = new ArrayList<>();
        for (RaspunsJucator raspuns : raspunsuri) {
            RaspunsIntrebareDTO dto = new RaspunsIntrebareDTO(
                raspuns.getId(),
                raspuns.getIntrebare().getText(),
                raspuns.getRaspunsJucator(),
                raspuns.getIntrebare().getRaspunsCorect(),
                raspuns.getPunctaj(),
                raspuns.isCorect()
            );
            raspunsuriDTO.add(dto);
        }
        
        return new ResponseEntity<>(raspunsuriDTO, HttpStatus.OK);
    }
}
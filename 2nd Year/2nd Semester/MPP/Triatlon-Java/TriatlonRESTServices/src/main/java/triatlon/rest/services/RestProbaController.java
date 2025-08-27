package triatlon.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triatlon.model.Proba;
import triatlon.persistence.IProbaRepository;

import java.util.ArrayList;
import java.util.List;

//?     NU MI-A MERS DELOC CU HIBERNATE :(

@RestController
@RequestMapping("/triatlon/probe")
public class RestProbaController {

    private final IProbaRepository probaRepository;

    @Autowired
    public RestProbaController(IProbaRepository probaRepository) {
        this.probaRepository = probaRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Proba> getAllProbe() {
        System.out.println("Getting all probe ... ");

        List<Proba> probe = new ArrayList<>();
        probaRepository.findAll().forEach(probe::add);
        return probe;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get proba by id " + id);

        Proba proba = probaRepository.findOne(id);

        if (proba == null) {
            return new ResponseEntity<>("Proba nu s-a gasit", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Proba>(proba, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Proba create(@RequestBody Proba proba) {
        System.out.println("Saving proba" + proba);
        probaRepository.save(proba);
        System.out.println(proba + "Saved");
        return proba;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Proba proba, @PathVariable Integer id) {
        System.out.println("Updating proba" + proba);
        if (!id.equals(proba.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        probaRepository.update(proba);
        System.out.println(proba + "Updated");
        return new ResponseEntity<>(proba, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting proba cu id " + id);
        Proba proba = probaRepository.findOne(id);
        if (proba == null) {
            return new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND);
        }
        try {
            probaRepository.delete(id);
            return new ResponseEntity<Proba>(HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println("Exception " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

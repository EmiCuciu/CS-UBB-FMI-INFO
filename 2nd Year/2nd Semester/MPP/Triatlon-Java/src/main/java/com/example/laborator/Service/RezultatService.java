package com.example.laborator.Service;

import com.example.laborator.Domain.Arbitru;
import com.example.laborator.Domain.Participant;
import com.example.laborator.Domain.Rezultat;
import com.example.laborator.Domain.TipProba;
import com.example.laborator.Repository.IRezultatRepository;
import com.example.laborator.Utils.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RezultatService extends Observable {
    private static final Logger logger = LogManager.getLogger();
    private final IRezultatRepository rezultatRepository;

    public RezultatService(IRezultatRepository rezultatRepository) {
        this.rezultatRepository = rezultatRepository;
    }

    public void addRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) {
        logger.info("Adding result for participant {}: {} points in {}",
                participant.getId(), punctaj, tipProba);

        Rezultat rezultat = new Rezultat(null, participant, arbitru, tipProba, punctaj);
        rezultatRepository.save(rezultat);

        // Update participant's score
        participant.setPunctajProba(tipProba, punctaj);

        // Notify observers about the change
        notifyObservers();
    }

    public List<Rezultat> getRezultateForProba(TipProba tipProba) {
        List<Rezultat> rezultate = StreamSupport.stream(rezultatRepository.findAll().spliterator(), false)
                .filter(r -> r.getTipProba() == tipProba)
                .sorted(Comparator.comparing(Rezultat::getPunctaj).reversed())
                .collect(Collectors.toList());
        return rezultate;
    }

}
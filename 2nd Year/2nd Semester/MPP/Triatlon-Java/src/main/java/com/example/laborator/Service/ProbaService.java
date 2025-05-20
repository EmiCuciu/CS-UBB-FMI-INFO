package com.example.laborator.Service;

import com.example.laborator.Domain.Arbitru;
import com.example.laborator.Domain.Proba;
import com.example.laborator.Domain.TipProba;
import com.example.laborator.Repository.IProbaRepository;
import com.example.laborator.Utils.Observable;

import java.util.ArrayList;
import java.util.List;

public class ProbaService extends Observable {
    private final IProbaRepository probaRepository;

    public ProbaService(IProbaRepository probaRepository) {
        this.probaRepository = probaRepository;
    }

    public List<Proba> getAllProbe() {
        List<Proba> probe = new ArrayList<>();
        probaRepository.findAll().forEach(probe::add);
        return probe;
    }

    public TipProba getProbaForArbitru(Arbitru arbitru) {
        for (Proba proba : getAllProbe()) {
            if (proba.getArbitru().getId().equals(arbitru.getId())) {
                System.out.println("Proba gasita: " + proba.getTipProba());
                return proba.getTipProba();
            }
        }
        return null;
    }
}
package org.example.apeleromane.Service;

import org.example.apeleromane.Domain.Localitate;
import org.example.apeleromane.Domain.Rau;
import org.example.apeleromane.Domain.TipCotaPericol;
import org.example.apeleromane.Repository.LocalitateRepository;
import org.example.apeleromane.Repository.RauRepository;
import org.example.apeleromane.Utils.Events.ChangeEventType;
import org.example.apeleromane.Utils.Events.CotaChangeEvent;
import org.example.apeleromane.Utils.Events.Event;
import org.example.apeleromane.Utils.Obs.Observable;
import org.example.apeleromane.Utils.Obs.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service implements Observable {
    private final RauRepository rauRepository;
    private final LocalitateRepository localitateRepository;
    private final List<Observer> observers = new ArrayList<>();

    public Service(RauRepository rauRepository, LocalitateRepository localitateRepository) {
        this.rauRepository = rauRepository;
        this.localitateRepository = localitateRepository;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observers.forEach(observer -> observer.update(event));
    }

    public List<Localitate> getAllLocalitati() {
        return localitateRepository.findAll();
    }

    public List<Localitate> getLocalitatiByTip(TipCotaPericol pericol){
        return getAllLocalitati().stream().filter(localitate -> localitate.calculeazaRisc() == pericol).collect(Collectors.toList());
    }

    public void updateRauCota(Integer rauID, double nouaCota){
        Rau rau = rauRepository.findOne(rauID);
        if(rau != null){
            rau.setCotaMedie(nouaCota);
            rauRepository.update(rau);
            notifyObservers(new CotaChangeEvent(ChangeEventType.COTA_SCHIMBATA, rau));
        }
    }

    public TipCotaPericol calculeazaRiscPentruLocalitate(String numeLocalitate, Rau rau, double cmdr, double cma){
        Localitate nouaLocalitate = new Localitate(null, numeLocalitate, rau, cmdr, cma);
        return nouaLocalitate.calculeazaRisc();
    }


    public List<Rau> getAllRauri(){
        return rauRepository.findAll();
    }

    public Rau getRauById(Integer id){
        return rauRepository.findOne(id);
    }

    public Rau updateRauCota_returnRau(Integer rauID, double nouaCota){
        Rau rau = rauRepository.findOne(rauID);
        if(rau != null){
            rau.setCotaMedie(nouaCota);
            Rau updatedRau = rauRepository.update(rau);
            notifyObservers(new CotaChangeEvent(ChangeEventType.COTA_SCHIMBATA, updatedRau));
            return updatedRau;
        }
        return null;
    }

    public Rau findRauByName(String numeRau) {
        return rauRepository.findByName(numeRau);
    }
}

package org.example.ati_v2.Service;

import org.example.ati_v2.Domain.Pacient;
import org.example.ati_v2.Domain.Pat;
import org.example.ati_v2.Domain.StatusPacient;
import org.example.ati_v2.Domain.TipPat;
import org.example.ati_v2.Repository.PacientRepository;
import org.example.ati_v2.Repository.PatRepository;
import org.example.ati_v2.Utils.Events.ChangeEventType;
import org.example.ati_v2.Utils.Events.Event;
import org.example.ati_v2.Utils.Obs.Observable;
import org.example.ati_v2.Utils.Obs.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Service implements Observable {
    private final PacientRepository pacientRepository;
    private final PatRepository patRepository;
    private final List<Observer> observers = new ArrayList<>();

    public Service(PatRepository patRepository, PacientRepository pacientRepository){
        this.patRepository = patRepository;
        this.pacientRepository = pacientRepository;
    }



    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observers.forEach(observer -> observer.update(event));
    }

    public void asigneazaPat(String cnpPacient, int idPat, boolean necesitaVentilatie){
        Pat pat = patRepository.findOne(idPat);
        Pacient pacient = pacientRepository.findOne(cnpPacient);

        if(pat == null || pacient == null){
            throw new IllegalArgumentException("Pat sau pacient invalid");
        }

        if(!pat.isLiber()){
            throw new IllegalArgumentException("Patul este deja ocupat");
        }

        if(necesitaVentilatie && !pat.hasVentilatie()){
            throw new IllegalArgumentException("Pacientul necesita ventilatie dar patul nu are acest suport");
        }

        pat.setPacientCNP(cnpPacient);
        pacient.setStatusPacient(StatusPacient.INTERNAT);

        patRepository.update(pat);
        pacientRepository.update(pacient);

        notifyObservers(new Event(ChangeEventType.PAT_MODIFICAT, pat));
    }

    public void elibereazaPat(String cnpPacient) {
        List<Pat> toatePat = patRepository.findAll();
        Pat patOcupat = toatePat.stream()
                .filter(p -> cnpPacient.equals(p.getPacientCNP()))
                .findFirst()
                .orElse(null);

        if (patOcupat == null){
            throw new IllegalArgumentException("Pacientul nu este internat");
        }

        Pacient pacient = pacientRepository.findOne(cnpPacient);
        if(pacient != null){
            pacient.setStatusPacient(StatusPacient.EXTERNAT);
            pacientRepository.update(pacient);
        }

        patOcupat.setPacientCNP(null);
        patRepository.update(patOcupat);

        notifyObservers(new Event(ChangeEventType.PAT_MODIFICAT, patOcupat));
    }


    public Map<TipPat, Integer> getStatusPaturiLibere(){
        return patRepository.getStatusPaturiLibere();
    }

    public List<Pacient> getPacientiInAsteptare(){
        return pacientRepository.getPacientInAsteptare();
    }

    public List<Pat> getPaturiByTip(TipPat tipPat){
        return patRepository.getPaturiByTip(tipPat);
    }

    public List<Pat> getAllPaturi(){
        return patRepository.findAll();
    }
}

package org.example.faptebune.Service;

import org.example.faptebune.Domain.Nevoie;
import org.example.faptebune.Domain.Orase;
import org.example.faptebune.Domain.Persoana;
import org.example.faptebune.Repository.IRepository;
import org.example.faptebune.Utils.Observable;
import org.example.faptebune.Utils.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private IRepository<Long, Persoana> personRepo;
    private IRepository<Long, Nevoie> needRepo;
    private Observable<Nevoie> needsObservable = new Observable<>();

    public Service(IRepository<Long, Persoana> personRepo, IRepository<Long, Nevoie> needRepo) {
        this.personRepo = personRepo;
        this.needRepo = needRepo;
    }

    public Persoana login(String username) {
        return StreamSupport.stream(personRepo.findAll().spliterator(), false)
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void register(String username, String password, String firstName, String lastName, Orase city) {
        Persoana person = new Persoana();
        person.setUsername(username);
        person.setParola(password);
        person.setPrenume(firstName);
        person.setNume(lastName);
        person.setOras(city);
        personRepo.save(person);
    }

    public void addNeed(String description, LocalDate deadline, Long personId) {
        Nevoie need = new Nevoie();
        need.setDescriere(description);
        need.setDeadline(String.valueOf(deadline));
        need.setOmInNevoie(personId);
        need.setStatus("Caut erou!");
        needRepo.save(need);
        needsObservable.notifyObservers();
    }

    public void assignHeroToNeed(Long needId, Long heroId) {
        Nevoie need = needRepo.findOne(needId);
        need.setOmSalvator(heroId);
        need.setStatus("Erou gasit!");
        needRepo.update(need);
        needsObservable.notifyObservers();
    }

    public List<Nevoie> getNeedsForCity(Orase city, Long excludePersonId) {
        return StreamSupport.stream(needRepo.findAll().spliterator(), false)
                .filter(need -> {
                    Persoana person = personRepo.findOne(need.getOmInNevoie());
                    return person.getOras() == city && (need.getOmSalvator() == null || !need.getOmSalvator().equals(excludePersonId));
                })
                .collect(Collectors.toList());
    }

    public List<Nevoie> getHeroNeeds(Long heroId) {
        return StreamSupport.stream(needRepo.findAll().spliterator(), false)
                .filter(need -> heroId.equals(need.getOmSalvator()))
                .collect(Collectors.toList());
    }

    public void addNeedObserver(Observer<Nevoie> observer) {
        needsObservable.addObserver(observer);
    }

    public String getAllUsernames() {
        return StreamSupport.stream(personRepo.findAll().spliterator(), false)
                .map(Persoana::getUsername)
                .collect(Collectors.joining(", "));
    }
}
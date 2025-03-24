package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.Medicament;
import com.example.laboratoriss.Repository.IMedicamentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicamentService extends AbstractService<Medicament> implements IMedicamentService {

    private final IMedicamentRepository repository;
    private static final Logger logger = LogManager.getLogger();

    public MedicamentService(IMedicamentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Medicament findOne(Integer id) {
        logger.traceEntry("finding medicament with id: {}", id);
        return repository.findOne(id);
    }

    @Override
    public Iterable<Medicament> getAll() {
        logger.traceEntry("getting all medicaments");
        return repository.findAll();
    }

    @Override
    public void save(Medicament medicament) {
        logger.traceEntry("saving medicament: {}", medicament);
        repository.save(medicament);
        notifyAdd(medicament);
        logger.traceExit();
    }

    @Override
    public void update(Medicament medicament) {
        logger.traceEntry("updating medicament: {}", medicament);
        repository.update(medicament);
        notifyUpdate(medicament);
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting medicament with id: {}", id);
        Medicament medicament = repository.findOne(id);
        if (medicament != null) {
            repository.delete(id);
            notifyDelete(medicament);
        }
        logger.traceExit();
    }
}
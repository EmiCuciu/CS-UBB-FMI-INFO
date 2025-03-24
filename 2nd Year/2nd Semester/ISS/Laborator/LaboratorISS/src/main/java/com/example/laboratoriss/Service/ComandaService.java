package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Repository.IComandaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ComandaService extends AbstractService<Comanda> implements IComandaService {

    private final IComandaRepository repository;
    private static final Logger logger = LogManager.getLogger();

    public ComandaService(IComandaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comanda findOne(Integer id) {
        logger.traceEntry("finding comanda with id: {}", id);
        return repository.findOne(id);
    }

    @Override
    public Iterable<Comanda> getAll() {
        logger.traceEntry("getting all comenzi");
        return repository.findAll();
    }

    @Override
    public List<Comanda> getByUser(User user) {
        logger.traceEntry("getting comenzi for user: {}", user);
        return repository.findByUser(user);
    }

    @Override
    public List<Comanda> getByStatus(Status status) {
        logger.traceEntry("getting comenzi with status: {}", status);
        return repository.findByStatus(status);
    }

    @Override
    public void save(Comanda comanda) {
        logger.traceEntry("saving comanda: {}", comanda);
        repository.save(comanda);
        notifyAdd(comanda);
        logger.traceExit();
    }

    @Override
    public void update(Comanda comanda) {
        logger.traceEntry("updating comanda: {}", comanda);
        repository.update(comanda);
        notifyUpdate(comanda);
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting comanda with id: {}", id);
        Comanda comanda = repository.findOne(id);
        if (comanda != null) {
            repository.delete(id);
            notifyDelete(comanda);
        }
        logger.traceExit();
    }

    @Override
    public void updateStatus(Integer comandaId, Status newStatus) {
        logger.traceEntry("updating status for comanda {} to {}", comandaId, newStatus);
        Comanda comanda = repository.findOne(comandaId);
        if (comanda != null) {
            comanda.setStatus(newStatus);
            repository.update(comanda);
            notifyUpdate(comanda);
        }
        logger.traceExit();
    }
}
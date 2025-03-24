package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;
import com.example.laboratoriss.Utils.Observer.Observable;

import java.util.List;

public interface IComandaService extends Observable<Comanda> {
    Comanda findOne(Integer id);

    Iterable<Comanda> getAll();

    List<Comanda> getByUser(User user);

    List<Comanda> getByStatus(Status status);

    void save(Comanda comanda);

    void update(Comanda comanda);

    void delete(Integer id);

    void updateStatus(Integer comandaId, Status newStatus);
}
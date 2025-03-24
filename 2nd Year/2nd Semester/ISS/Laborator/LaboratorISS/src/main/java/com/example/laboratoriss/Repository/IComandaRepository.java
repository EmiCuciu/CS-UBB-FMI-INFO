package com.example.laboratoriss.Repository;

import com.example.laboratoriss.Domain.Comanda;
import com.example.laboratoriss.Domain.Status;
import com.example.laboratoriss.Domain.User;

import java.util.List;

public interface IComandaRepository extends IRepository<Integer, Comanda> {
    List<Comanda> findByUser(User user);

    List<Comanda> findByStatus(Status status);
}

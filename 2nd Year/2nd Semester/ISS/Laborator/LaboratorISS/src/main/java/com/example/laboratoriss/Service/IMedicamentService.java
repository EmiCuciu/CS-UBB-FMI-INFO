package com.example.laboratoriss.Service;

import com.example.laboratoriss.Domain.Medicament;
import com.example.laboratoriss.Utils.Observer.Observable;

public interface IMedicamentService extends Observable<Medicament> {
    Medicament findOne(Integer id);

    Iterable<Medicament> getAll();

    void save(Medicament medicament);

    void update(Medicament medicament);

    void delete(Integer id);
}
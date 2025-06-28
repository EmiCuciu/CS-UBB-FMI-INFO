package persistence.repository.IRepositories;

import domain.Jucator;
import persistence.repository.IRepository;

import java.util.Optional;

public interface IRepoJucatori extends IRepository<Integer, Jucator> {
    Optional<Jucator> findByNume(String nume);
}
